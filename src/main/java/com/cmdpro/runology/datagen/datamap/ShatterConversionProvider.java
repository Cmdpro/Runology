package com.cmdpro.runology.datagen.datamap;

import com.cmdpro.runology.datamaps.RunologyDatamaps;
import com.cmdpro.runology.datamaps.ShatterConversionMap;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ShatterConversionProvider extends DataMapProvider {

    public ShatterConversionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        this.builder(RunologyDatamaps.SHATTER_CONVERSION)
                .add(getHolder(Blocks.DIRT), createMap(BlockRegistry.OTHERWORLDLY_DIRT.get()), false)
                .add(getHolder(Blocks.GRASS_BLOCK), setPlaceAbove(createMap(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get()), createGrassPlaceAbove()), false)
                .add(BlockTags.BASE_STONE_OVERWORLD, createMap(BlockRegistry.OTHERWORLDLY_STONE.get()), false)
                .add(getHolder(Blocks.SHORT_GRASS), createMap(BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get()), false)
                .add(getHolder(Blocks.TALL_GRASS), createMap(BlockRegistry.TALL_OTHERWORLDLY_GRASS.get()), false)
                .add(getHolder(Blocks.SAND), createMap(BlockRegistry.OTHERWORLDLY_SAND.get()), false)
                .add(getHolder(Blocks.SANDSTONE), createMap(BlockRegistry.OTHERWORLDLY_SANDSTONE.get()), false);
    }

    public ShatterConversionMap.PlaceAboveConfig createGrassPlaceAbove() {
        return createPlaceAbove(3, BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get(), BlockRegistry.TALL_OTHERWORLDLY_GRASS.get());
    }


    public ShatterConversionMap createMap(Block... blocks) {
        return new ShatterConversionMap(Arrays.stream(blocks).toList(), Optional.empty());
    }
    public ShatterConversionMap setPlaceAbove(ShatterConversionMap map, ShatterConversionMap.PlaceAboveConfig config) {
        return new ShatterConversionMap(map.convertTo(), Optional.of(config));
    }
    public ShatterConversionMap.PlaceAboveConfig createPlaceAbove(int rarity, Block... blocks) {
        return new ShatterConversionMap.PlaceAboveConfig(Arrays.stream(blocks).toList(), rarity);
    }
    public ShatterConversionMap.PlaceAboveConfig createPlaceAbove(Block... blocks) {
        return new ShatterConversionMap.PlaceAboveConfig(Arrays.stream(blocks).toList(), 0);
    }
    private Holder<Block> getHolder(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
