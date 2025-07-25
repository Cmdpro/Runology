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

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShatterConversionProvider extends DataMapProvider {

    public ShatterConversionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        this.builder(RunologyDatamaps.SHATTER_CONVERSION)
                .add(getHolder(Blocks.DIRT), new ShatterConversionMap(List.of(BlockRegistry.OTHERWORLDLY_DIRT.get())), false)
                .add(getHolder(Blocks.GRASS_BLOCK), new ShatterConversionMap(List.of(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get())), false)
                .add(BlockTags.BASE_STONE_OVERWORLD, new ShatterConversionMap(List.of(BlockRegistry.OTHERWORLDLY_STONE.get())), false)
                .add(getHolder(Blocks.SHORT_GRASS), new ShatterConversionMap(List.of(BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get())), false)
                .add(getHolder(Blocks.TALL_GRASS), new ShatterConversionMap(List.of(BlockRegistry.TALL_OTHERWORLDLY_GRASS.get())), false);
    }
    private Holder<Block> getHolder(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
