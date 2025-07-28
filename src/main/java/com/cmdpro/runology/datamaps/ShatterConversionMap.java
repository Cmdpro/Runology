package com.cmdpro.runology.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ShatterConversionMap(List<Block> convertTo, Optional<PlaceAboveConfig> placeAbove) {
    public static final Codec<ShatterConversionMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("convert_to").forGetter(ShatterConversionMap::convertTo),
            PlaceAboveConfig.CODEC.optionalFieldOf("place_above").forGetter(ShatterConversionMap::placeAbove)
    ).apply(instance, ShatterConversionMap::new));
    public record PlaceAboveConfig(List<Block> blocks, int rarity) {
        public static final Codec<PlaceAboveConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
               BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("blocks").forGetter(PlaceAboveConfig::blocks),
                Codec.INT.fieldOf("rarity").forGetter(PlaceAboveConfig::rarity)
        ).apply(instance, PlaceAboveConfig::new));
    }
}