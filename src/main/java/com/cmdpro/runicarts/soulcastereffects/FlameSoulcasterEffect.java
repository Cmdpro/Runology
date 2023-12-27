package com.cmdpro.runicarts.soulcastereffects;

import com.cmdpro.runicarts.api.SoulcasterEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class FlameSoulcasterEffect extends SoulcasterEffect {
    public FlameSoulcasterEffect() {
        soulCost = 1;
        color = new Color(1f, 0.5f, 0f);
    }

    @Override
    public void hitEntity(LivingEntity caster, LivingEntity victim, int amount) {
        super.hitEntity(caster, victim, amount);
        victim.setRemainingFireTicks(victim.getRemainingFireTicks()+(40*amount));
    }
}
