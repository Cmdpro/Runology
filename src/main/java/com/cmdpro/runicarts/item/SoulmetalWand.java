package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.api.Wand;
import com.cmdpro.runicarts.entity.SpellProjectile;

public class SoulmetalWand extends Wand {

    public SoulmetalWand(Properties properties) {
        super(properties, 1);
    }

    @Override
    public void customWandEffects(SpellProjectile proj) {
        super.customWandEffects(proj);
        proj.setDeltaMovement(proj.getDeltaMovement().multiply(2, 2, 2));
    }
}
