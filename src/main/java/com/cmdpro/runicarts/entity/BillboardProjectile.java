package com.cmdpro.runicarts.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.awt.*;

public abstract class BillboardProjectile extends Projectile {
    protected BillboardProjectile(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public Color getColor() {
        return Color.WHITE;
    }
    public abstract ResourceLocation getSprite();
    public float getOffset() {
        return 0.4f;
    }
    public float getScale() {
        return 1f;
    }

    @Override
    protected void defineSynchedData() {

    }
}
