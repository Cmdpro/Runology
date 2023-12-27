package com.cmdpro.runicarts.soulcastereffects;

import com.cmdpro.runicarts.api.SoulcasterEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class IceSoulcasterEffect extends SoulcasterEffect {
    public IceSoulcasterEffect() {
        soulCost = 1;
        color = new Color(0.5f, 0.5f, 1f);
    }

    @Override
    public void hitEntity(LivingEntity caster, LivingEntity victim, int amount) {
        super.hitEntity(caster, victim, amount);
        victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (5*20)*amount, 1));
    }
}
