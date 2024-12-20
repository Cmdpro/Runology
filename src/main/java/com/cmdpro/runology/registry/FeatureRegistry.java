package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.worldgen.features.ShatterFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE,
            Runology.MODID);
    public static final Supplier<Feature<NoneFeatureConfiguration>> SHATTER = register("shatter", () -> new ShatterFeature(NoneFeatureConfiguration.CODEC));
    private static <T extends Feature<?>> Supplier<T> register(final String name, final Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }
}
