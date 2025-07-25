package com.cmdpro.runology.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record ShatterConversionMap(List<Block> convertTo) {
    public static final Codec<ShatterConversionMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("convert_to").forGetter(ShatterConversionMap::convertTo)
    ).apply(instance, ShatterConversionMap::new));
}