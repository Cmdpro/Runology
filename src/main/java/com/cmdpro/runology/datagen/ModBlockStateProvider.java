package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Runology.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockRegistry.SHATTERSTONE);
        blockWithItem(BlockRegistry.RUNE_HEAT_SHATTERSTONE);
        blockWithItem(BlockRegistry.RUNE_SHAPE_SHATTERSTONE);
        blockWithItem(BlockRegistry.RUNE_FROST_SHATTERSTONE);
        blockWithItem(BlockRegistry.RUNE_MOTION_SHATTERSTONE);
        blockWithItem(BlockRegistry.SHATTERED_BLOCK);

        blockWithItem(BlockRegistry.SHATTERSTONE_BRICKS);
        stairsBlock((StairBlock) BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get(), Runology.locate("block/shatterstone_bricks"));
        slabBlock((SlabBlock) BlockRegistry.SHATTERSTONE_BRICK_SLAB.get(), Runology.locate("block/shatterstone_bricks"), Runology.locate("block/shatterstone_bricks"));
        wallBlock((WallBlock) BlockRegistry.SHATTERSTONE_BRICK_WALL.get(), Runology.locate("block/shatterstone_bricks"));
    }

    private void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}