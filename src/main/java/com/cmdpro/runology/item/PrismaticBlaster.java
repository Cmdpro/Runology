package com.cmdpro.runology.item;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;

import java.util.List;

public class PrismaticBlaster extends SwordItem {
    public PrismaticBlaster(Properties pProperties) {
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
            if (pRemainingUseDuration % 10 == 0 && pStack.hasTag()) {
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
            if (pRemainingUseDuration <= 0) {
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
                pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS);
                itemstack.getOrCreateTag().putInt("charge", 0);
            } else {
                return super.use(pLevel, pPlayer, pHand);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 70;
    }
}
