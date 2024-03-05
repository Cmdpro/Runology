package com.cmdpro.runology.item;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.RunologyUtil;
import com.ibm.icu.impl.TimeZoneGenericNames;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.List;

public class LanternOfFlames extends Item {
    public LanternOfFlames(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        if (!pLivingEntity.level().isClientSide()) {
            ((ServerLevel) pLevel).sendParticles(ParticleTypes.FLAME, pLivingEntity.getBoundingBox().getCenter().x, pLivingEntity.getBoundingBox().getCenter().y, pLivingEntity.getBoundingBox().getCenter().z, 20, 0, 0, 0, 1);
            for (Entity i : pLevel.getEntities(pLivingEntity, AABB.ofSize(pLivingEntity.getBoundingBox().getCenter(), 5, 5, 5))) {
                if (i.position().distanceTo(pLivingEntity.getBoundingBox().getCenter()) <= 5) {
                    if (i.getRemainingFireTicks() <= 50) {
                        i.setRemainingFireTicks(50);
                        i.hurt(pLivingEntity.damageSources().magic(), 5f);
                    }
                }
            }
            pStack.setDamageValue(pStack.getDamageValue()+1);
            if (pStack.getDamageValue() >= pStack.getMaxDamage()-1) {
                pLivingEntity.stopUsingItem();
            }
        }
    }
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.level().isClientSide()) {
            if (entity.level().getBlockState(entity.blockPosition()).is(BlockTags.FIRE) || entity.level().getFluidState(entity.blockPosition()).is(FluidTags.LAVA)) {
                if (stack.getDamageValue() > 0) {
                    stack.setDamageValue(stack.getDamageValue() - 1);
                    ((ServerLevel) entity.level()).sendParticles(ParticleTypes.FLAME, entity.position().x, entity.position().y, entity.position().z, 5, 0, 0, 0, 0.5);
                }
            }
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() < itemstack.getMaxDamage()-1) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
        return super.use(pLevel, pPlayer, pHand);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
