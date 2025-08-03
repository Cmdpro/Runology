package com.cmdpro.runology.entity;

import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownOtherworldBombProjectile extends ThrowableItemProjectile {
    public ThrownOtherworldBombProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ThrownOtherworldBombProjectile(LivingEntity pShooter, Level pLevel) {
        super(EntityRegistry.OTHERWORLD_BOMB.get(), pShooter, pLevel);
    }
    public ThrownOtherworldBombProjectile(Level pLevel, double pX, double pY, double pZ) {
        super(EntityRegistry.OTHERWORLD_BOMB.get(), pX, pY, pZ, pLevel);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.OTHERWORLD_BOMB.get();
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide) {
            ShatterBlockEntity.createExplosion(level(), blockPosition(), 16);
            remove(Entity.RemovalReason.KILLED);
        }
    }
}
