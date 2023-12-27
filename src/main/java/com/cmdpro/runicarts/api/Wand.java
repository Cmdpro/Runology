package com.cmdpro.runicarts.api;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.entity.SpellProjectile;
import com.cmdpro.runicarts.init.EntityInit;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wand extends Item {
    public float castCostMultiplier;

    public Wand(Properties pProperties, float castCostMultiplier) {
        super(pProperties);
        this.castCostMultiplier = castCostMultiplier;
    }

    public float getCastCostMultiplier(Player player, ItemStack stack) {
        return castCostMultiplier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("types")) {
                ListTag types = (ListTag) pStack.getTag().get("types");
                types.forEach((i) -> {
                    CompoundTag i2 = (CompoundTag) i;
                    String id = i2.getString("id");
                    pTooltipComponents.add(Component.translatable("object.soulcastereffect." + id.replace(":", ".")).withStyle(ChatFormatting.GRAY));
                });
            }
            if (pStack.getTag().contains("amplifier")) {
                pTooltipComponents.add(Component.translatable("item.runicarts.wand.amplifier", pStack.getTag().getInt("amplifier")).withStyle(ChatFormatting.GRAY));
            }
            if (pStack.hasTag()) {
                if (pStack.getTag().contains("types")) {
                    ListTag types = (ListTag) pStack.getTag().get("types");
                    int soulCost = 0;
                    List<SoulcasterEffect> effects = new ArrayList<>();
                    for (Tag i : types) {
                        int times = 1;
                        if (pStack.getTag().contains("amplifier")) {
                            times = pStack.getTag().getInt("amplifier");
                        }
                        CompoundTag i2 = (CompoundTag) i;
                        SoulcasterEffect effect = RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY.get().getValue(ResourceLocation.of(i2.getString("id"), ':'));
                        for (int o = 0; o < times; o++) {
                            soulCost += effect.soulCost;
                            effects.add(effect);
                        }
                    }
                    pTooltipComponents.add(Component.translatable("item.runicarts.wand.cost", soulCost).withStyle(ChatFormatting.AQUA));
                }
            }
        }
        pTooltipComponents.add(Component.translatable("item.runicarts.wand.costmultiplier", getCastCostMultiplier(Minecraft.getInstance().player, pStack)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            if (stack.hasTag()) {
                if (stack.getTag().contains("types")) {
                    ListTag types = (ListTag) stack.getTag().get("types");
                    int soulCost = 0;
                    List<SoulcasterEffect> effects = new ArrayList<>();
                    for (Tag i : types) {
                        int times = 1;
                        if (stack.getTag().contains("amplifier")) {
                            times = stack.getTag().getInt("amplifier");
                        }
                        CompoundTag i2 = (CompoundTag) i;
                        SoulcasterEffect effect = RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY.get().getValue(ResourceLocation.of(i2.getString("id"), ':'));
                        for (int o = 0; o < times; o++) {
                            soulCost += effect.soulCost;
                            effects.add(effect);
                        }
                    }
                    soulCost *= getCastCostMultiplier(pPlayer, stack);
                    final int soulCostFinal = soulCost;
                    pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                        if (data.getSouls() >= soulCostFinal) {
                            HashMap<SoulcasterEffect, Integer> effects2 = new HashMap<>();
                            for (SoulcasterEffect i : effects) {
                                if (!effects2.containsKey(i)) {
                                    effects2.put(i, 1);
                                } else {
                                    effects2.put(i, effects2.get(i)+1);
                                }
                            }
                            effects2.forEach((k, v) -> {
                                k.cast(pPlayer, v);
                            });
                            data.setSouls(data.getSouls()-soulCostFinal);
                            if (!pPlayer.level().isClientSide()){
                                SpellProjectile arrow = new SpellProjectile(EntityInit.SPELLPROJECTILE.get(), pPlayer, pLevel);
                                arrow.setDeltaMovement(pPlayer.getLookAngle().multiply(0.5, 0.5, 0.5));
                                arrow.amplifier = stack.getTag().getInt("amplifier");
                                arrow.effects = effects;
                                customWandEffects(arrow);
                                pPlayer.level().addFreshEntity(arrow);
                            }
                        }
                    });
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    public void customWandEffects(SpellProjectile proj) {}
}
