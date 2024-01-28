package com.cmdpro.runology.entity;

import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.client.renderer.entity.SpectralArrowRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class PurityArrow extends AbstractArrow {
    public PurityArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public PurityArrow(Level pLevel, LivingEntity shooter) {
        super(EntityInit.PURITYARROW.get(), shooter, pLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!pResult.getEntity().level().isClientSide) {
            if (pResult.getEntity() instanceof Shatter shatter) {
                shatter.playSound(SoundEvents.GENERIC_EXPLODE);
                ((ServerLevel) shatter.level()).sendParticles(ParticleTypes.EXPLOSION_EMITTER, pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, 1, 0, 0, 0, 0);
                shatter.remove(RemovalReason.KILLED);
                remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity instanceof Shatter || super.canHitEntity(entity);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemInit.PURITYARROW.get());
    }
}
