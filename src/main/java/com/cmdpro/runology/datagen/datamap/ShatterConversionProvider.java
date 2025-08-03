package com.cmdpro.runology.datagen.datamap;

import com.cmdpro.runology.datamaps.RunologyDatamaps;
import com.cmdpro.runology.datamaps.BlockShatterConversionMap;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ShatterConversionProvider extends DataMapProvider {

    public ShatterConversionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        this.builder(RunologyDatamaps.BLOCK_SHATTER_CONVERSION)
                .add(getHolder(Blocks.DIRT), createMap(BlockRegistry.OTHERWORLDLY_DIRT.get()), false)
                .add(getHolder(Blocks.GRASS_BLOCK), setPlaceAbove(createMap(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get()), createGrassPlaceAbove()), false)
                .add(BlockTags.BASE_STONE_OVERWORLD, createMap(BlockRegistry.OTHERWORLDLY_STONE.get()), false)
                .add(getHolder(Blocks.SHORT_GRASS), createMap(BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get()), false)
                .add(getHolder(Blocks.TALL_GRASS), createMap(BlockRegistry.TALL_OTHERWORLDLY_GRASS.get()), false)
                .add(getHolder(Blocks.SAND), createMap(BlockRegistry.OTHERWORLDLY_SAND.get()), false)
                .add(getHolder(Blocks.SANDSTONE), createMap(BlockRegistry.OTHERWORLDLY_SANDSTONE.get()), false);
    }

    public BlockShatterConversionMap.PlaceAboveConfig createGrassPlaceAbove() {
        return createPlaceAbove(3, BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get(), BlockRegistry.TALL_OTHERWORLDLY_GRASS.get());
    }


    public BlockShatterConversionMap createMap(Block... blocks) {
        return new BlockShatterConversionMap(Arrays.stream(blocks).toList(), Optional.empty());
    }
    public BlockShatterConversionMap setPlaceAbove(BlockShatterConversionMap map, BlockShatterConversionMap.PlaceAboveConfig config) {
        return new BlockShatterConversionMap(map.convertTo(), Optional.of(config));
    }
    public BlockShatterConversionMap.PlaceAboveConfig createPlaceAbove(int rarity, Block... blocks) {
        return new BlockShatterConversionMap.PlaceAboveConfig(Arrays.stream(blocks).toList(), rarity);
    }
    public BlockShatterConversionMap.PlaceAboveConfig createPlaceAbove(Block... blocks) {
        return new BlockShatterConversionMap.PlaceAboveConfig(Arrays.stream(blocks).toList(), 0);
    }
    private Holder<Block> getHolder(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
