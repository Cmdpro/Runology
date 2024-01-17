package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class DimensionInit {
    public static final ResourceKey<Level> SHATTERREALM = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(Runology.MOD_ID, "shatterrealm"));
    public static final ResourceKey<DimensionType> SHATTERREALMTYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(Runology.MOD_ID, "shatterrealm"));
}
