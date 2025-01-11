package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeRegistry {
    public static final ResourceKey<DamageType> shatterZap = damageType("shatter_zap");

    private static ResourceKey<DamageType> damageType(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Runology.locate(name));
    }
}
