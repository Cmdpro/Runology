package com.cmdpro.runology.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;

public class ModFoods {
    public static final FoodProperties PURIFIEDFLESH = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.1F).meat().build();
}
