package com.cmdpro.runology.item;

import com.cmdpro.runology.entity.FireballProjectile;
import com.cmdpro.runology.entity.PrismaticBulletProjectile;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public class PrismaticBlaster extends Item {
    public PrismaticBlaster(Properties pProperties) {
        super(pProperties);
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
            if (pRemainingUseDuration % 8 == 0 && pRemainingUseDuration < 64 && pStack.hasTag()) {
                if (pStack.getTag().contains("charge") && pStack.getTag().getInt("charge") < 7) {
                    pStack.getTag().putInt("charge", pStack.getTag().getInt("charge")+1);
                    if (pStack.getTag().getInt("charge") >= 7) {
                        pLevel.playSound(null, pLivingEntity.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS);
                        pLivingEntity.stopUsingItem();
                    }
                } else {
                    pStack.getTag().putInt("charge", 1);
                }
                pLevel.playSound(null, pLivingEntity.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS);
            }
            if (pRemainingUseDuration <= 0 || !pLivingEntity.isShiftKeyDown()) {
                pLivingEntity.stopUsingItem();
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isShiftKeyDown()) {
            if (!itemstack.hasTag() || !itemstack.getTag().contains("charge") || itemstack.getTag().getInt("charge") < 7) {
                pPlayer.startUsingItem(pHand);
            } else {
                return super.use(pLevel, pPlayer, pHand);
            }
        } else {
            boolean canShoot = true;
            if (itemstack.hasTag()) {
                if (itemstack.getTag().contains("charge")) {
                    if (itemstack.getTag().getInt("charge") <= 0) {
                        canShoot = false;
                    }
                } else {
                    canShoot = false;
                }
            } else {
                canShoot = false;
            }
            if (canShoot) {
                Color[] colors = new Color[] {
                        new Color(0xe64539),
                        new Color(0xd1884c),
                        new Color(0xffee83),
                        new Color(0x63ab3f),
                        new Color(0x647aff),
                        new Color(0x3951e0),
                        new Color(0x7d2da0)
                };
                int charge = itemstack.getOrCreateTag().contains("charge") ? itemstack.getOrCreateTag().getInt("charge") : 0;
                int randomForward = pPlayer.getRandom().nextInt(0, charge);
                pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS);
                for (int i = 0; i < charge; i++) {
                    PrismaticBulletProjectile bullet = new PrismaticBulletProjectile(EntityInit.PRISMATICBULLET.get(), pPlayer, pLevel);
                    bullet.color = colors[i];
                    if (randomForward != i) {
                        bullet.setDeltaMovement(calculateViewVector(pPlayer.getXRot()+(RandomUtils.nextFloat(0f, 20f)-10f), pPlayer.getYRot()+(RandomUtils.nextFloat(0f, 20f)-10f)).multiply(1.6f, 1.6f, 1.6f));
                    } else {
                        bullet.setDeltaMovement(calculateViewVector(pPlayer.getXRot(), pPlayer.getYRot()).multiply(1.6f, 1.6f, 1.6f));
                    }
                    pLevel.addFreshEntity(bullet);
                }
                itemstack.getOrCreateTag().putInt("charge", 0);
            } else {
                return super.use(pLevel, pPlayer, pHand);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 64;
    }
}
