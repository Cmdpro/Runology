package com.cmdpro.runology.api;

import com.cmdpro.runology.init.SpellInit;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Staff extends Item {
    public int magicLevel;
    public float maxRunicEnergy;
    public Staff(Properties properties, int magicLevel, float maxRunicEnergy) {
        super(properties);
        this.magicLevel = magicLevel;
        this.maxRunicEnergy = maxRunicEnergy;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.runology.staff.tier", magicLevel));
        if (pStack.hasTag() && pStack.getTag().contains("runicEnergy")) {
            for (String i : ((CompoundTag) pStack.getTag().get("runicEnergy")).getAllKeys()) {
                float amount = ((CompoundTag) pStack.getTag().get("runicEnergy")).getFloat(i);
                if (amount > 0) {
                    Color color = RunologyUtil.RUNIC_ENERGY_TYPES_REGISTRY.get().getValue(ResourceLocation.tryParse(i)).color;
                    pTooltipComponents.add(Component.translatable("container.runology.runicworkbench.runicenergyamount", Math.ceil(amount), maxRunicEnergy, Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i)))).withStyle(style -> style.withColor(color.getRGB())));
                }
            }
        }
        if (pStack.hasTag() && pStack.getTag().contains("upgrades")) {
            pTooltipComponents.add(Component.empty());
            for (Tag i : ((ListTag) pStack.getTag().get("upgrades"))) {
                ResourceLocation id = ResourceLocation.tryParse(((CompoundTag)i).getString("upgrade"));
                pTooltipComponents.add(Component.translatable("upgrade." + id.getNamespace() + "." + id.getPath()));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player player) {
            if (pStack.hasTag()) {
                CompoundTag tag = pStack.getTag();
                if (tag.contains("upgrades")) {
                    ListTag tag2 = (ListTag) tag.get("upgrades");
                    for (Tag i : tag2) {
                        CompoundTag tag3 = (CompoundTag) i;
                        RunologyUtil.SPELLCASTING_UPGRADES_REGISTRY.get().getValue(ResourceLocation.tryParse(tag3.getString("upgrade"))).tick(player, pStack);
                    }
                }
            }
        }
    }
    public Spell getSpell(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof SpellCrystal crystal) {
            Spell spell = crystal.getSpell();
            if (spell.staffCastable()) {
                return spell;
            }
        }
        return null;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Spell spell = getSpell(pLevel, pPlayer, pUsedHand);
            if (spell != null) {
                if (spell.staffCastable() && magicLevel >= spell.magicLevel()) {
                    HashMap<ResourceLocation, Float> cost = spell.getCost();
                    if (pPlayer.getItemInHand(pUsedHand).hasTag()) {
                        CompoundTag tag = pPlayer.getItemInHand(pUsedHand).getTag();
                        if (tag.contains("upgrades")) {
                            ListTag tag2 = (ListTag) tag.get("upgrades");
                            for (Tag i : tag2) {
                                CompoundTag tag3 = (CompoundTag) i;
                                cost = RunologyUtil.SPELLCASTING_UPGRADES_REGISTRY.get().getValue(ResourceLocation.tryParse(tag3.getString("upgrade"))).costChanges(cost);
                            }
                        }
                    }
                    if (pPlayer.getItemInHand(pUsedHand).hasTag() && pPlayer.getItemInHand(pUsedHand).getTag().contains("runicEnergy")) {
                        boolean hasAll = true;
                        CompoundTag tag = ((CompoundTag)pPlayer.getItemInHand(pUsedHand).getTag().get("runicEnergy"));
                        for (String i : tag.getAllKeys()) {
                            for (Map.Entry<ResourceLocation, Float> o : cost.entrySet()) {
                                if (i.equals(o.getKey().toString())) {
                                    if (tag.getFloat(i) < o.getValue()) {
                                        hasAll = false;
                                    }
                                } else if (!tag.getAllKeys().contains(o.getKey().toString())) {
                                    hasAll = false;
                                }
                            }
                        }
                        if (hasAll) {
                            if (spell.cast(pPlayer, true, false)) {
                                for (String i : tag.getAllKeys()) {
                                    for (Map.Entry<ResourceLocation, Float> o : cost.entrySet()) {
                                        if (i.equals(o.getKey().toString())) {
                                            tag.putFloat(i, tag.getFloat(i) - o.getValue());
                                        }
                                    }
                                }
                                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
                            }
                        }
                    }
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
