package com.cmdpro.runicarts.soulcastereffects;

import com.cmdpro.runicarts.api.SoulcasterEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class DeathSoulcasterEffect extends SoulcasterEffect {
    public DeathSoulcasterEffect() {
        soulCost = 1;
        color = new Color(1f, 0f, 0f);
    }

    @Override
    public void hitEntity(LivingEntity caster, LivingEntity victim, int amount) {
        super.hitEntity(caster, victim, amount);
        victim.hurt(caster.level().damageSources().mobAttack(caster), 2*amount);
    }
}
