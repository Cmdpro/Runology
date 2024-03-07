package com.cmdpro.runology.api;

import com.cmdpro.runology.init.SpellInit;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Gauntlet extends Item {
    public int magicLevel;
    public float maxRunicEnergy;
    public Gauntlet(Properties properties, int magicLevel, float maxRunicEnergy) {
        super(properties);
        this.magicLevel = magicLevel;
        this.maxRunicEnergy = maxRunicEnergy;
    }
    public Spell getSpell(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof SpellCrystal crystal) {
            Spell spell = crystal.getSpell();
            if (spell.gauntletCastable()) {
                return spell;
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.runology.gauntlet.tier", magicLevel));
        if (pStack.hasTag() && pStack.getTag().contains("runicEnergy")) {
            for (String i : ((CompoundTag) pStack.getTag().get("runicEnergy")).getAllKeys()) {
                float amount = ((CompoundTag) pStack.getTag().get("runicEnergy")).getFloat(i);
                if (amount > 0) {
                    Color color = RunologyUtil.RUNIC_ENERGY_TYPES_REGISTRY.get().getValue(ResourceLocation.tryParse(i)).color;
                    pTooltipComponents.add(Component.translatable("container.runology.runicworkbench.runicenergyamount", amount, maxRunicEnergy, Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i)))).withStyle(style -> style.withColor(color.getRGB())));
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
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Spell spell = getSpell(pLevel, pPlayer, pUsedHand);
            if (spell != null) {
                if (spell.gauntletCastable() && magicLevel >= spell.magicLevel()) {
                    if (pPlayer.getItemInHand(pUsedHand).hasTag() && pPlayer.getItemInHand(pUsedHand).getTag().contains("runicEnergy")) {
                        boolean hasAll = true;
                        CompoundTag tag = ((CompoundTag)pPlayer.getItemInHand(pUsedHand).getTag().get("runicEnergy"));
                        for (String i : tag.getAllKeys()) {
                            for (Map.Entry<ResourceLocation, Float> o : spell.getCost().entrySet()) {
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
                            for (String i : tag.getAllKeys()) {
                                for (Map.Entry<ResourceLocation, Float> o : spell.getCost().entrySet()) {
                                    if (i.equals(o.getKey().toString())) {
                                        tag.putFloat(i, tag.getFloat(i)-o.getValue());
                                    }
                                }
                            }
                            spell.cast(pPlayer, false, true);
                            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
                        }
                    }
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
