package com.cmdpro.runology.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import org.checkerframework.checker.units.qual.C;
import org.joml.Vector3f;

import java.util.List;

public class AncientDragonsBlade extends SwordItem {
    public AncientDragonsBlade(Properties pProperties) {
        super(ModTiers.ANCIENTDRAGONSBLADE, 1, -2.4F, pProperties);
    }
    public Vec3 calculateViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        if (!pLivingEntity.level().isClientSide()) {
            for (int i = 0; i < 10; i++) {
                for (int o = -4; o <= 4; o++) {
                    Vec3 vec = calculateViewVector(pLivingEntity.getXRot(), pLivingEntity.getYRot() + (o * 5)).multiply(((float) i) / 2, ((float) i) / 2, ((float) i) / 2);
                    Vec3 pos = pLivingEntity.getBoundingBox().getCenter().add(0, 0.25f, 0).add(vec);
                    List<LivingEntity> entities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pLivingEntity, AABB.ofSize(pos, 0.5f, 0.5f, 0.5f));
                    for (LivingEntity p : entities) {
                        p.hurt(pLivingEntity.damageSources().mobAttack(pLivingEntity), 8);
                    }
                }
            }
            if (pRemainingUseDuration <= 0) {
                pLivingEntity.stopUsingItem();
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int o = -4; o <= 4; o++) {
                    Vec3 vec = calculateViewVector(pLivingEntity.getXRot(), pLivingEntity.getYRot() + (o * 5)).multiply(((float) i) / 2, ((float) i) / 2, ((float) i) / 2);
                    Vec3 pos = pLivingEntity.getBoundingBox().getCenter().add(0, 0.25f, 0).add(vec);
                    DustParticleOptions options = new DustParticleOptions(new Vector3f(1f, 0f, 1f), 1);
                    float movement = 0.025f;
                    for (int p = 0; p < 4; p++) {
                        pLevel.addParticle(options, pos.x+(RandomUtils.nextFloat(0f, 0.2f)-0.1f), pos.y+(RandomUtils.nextFloat(0f, 0.2f)-0.1f), pos.z+(RandomUtils.nextFloat(0f, 0.2f)-0.1f), movement, movement, movement);
                    }

                }
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(stack.getItem(), 100);
        }
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }
}
