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
        blockWithItem(BlockRegistry.SHATTERED_CRYSTAL_BLOCK);
        blockWithItem(BlockRegistry.BUDDING_SHATTERED_CRYSTAL);

        shatteredCrystalCluster(BlockRegistry.SMALL_SHATTERED_CRYSTAL_BUD);
        shatteredCrystalCluster(BlockRegistry.MEDIUM_SHATTERED_CRYSTAL_BUD);
        shatteredCrystalCluster(BlockRegistry.LARGE_SHATTERED_CRYSTAL_BUD);
        shatteredCrystalCluster(BlockRegistry.SHATTERED_CRYSTAL_CLUSTER);

        blockWithItem(BlockRegistry.SHATTERSTONE_BRICKS);
        stairsBlock((StairBlock) BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get(), Runology.locate("block/shatterstone_bricks"));
        slabBlock((SlabBlock) BlockRegistry.SHATTERSTONE_BRICK_SLAB.get(), Runology.locate("block/shatterstone_bricks"), Runology.locate("block/shatterstone_bricks"));
        wallBlock((WallBlock) BlockRegistry.SHATTERSTONE_BRICK_WALL.get(), Runology.locate("block/shatterstone_bricks"));
    }

    private void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void shatteredCrystalCluster(Supplier<Block> blockRegistryObject) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get());
        BlockModelBuilder model = models().cross(loc.getPath(), Runology.locate(ModelProvider.BLOCK_FOLDER + "/" + loc.getPath())).renderType("cutout");
        itemModels().withExistingParent(loc.getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                Runology.locate("block/" + loc.getPath()));
        getVariantBuilder(blockRegistryObject.get())
                .partialState().with(AmethystClusterBlock.FACING, Direction.DOWN).modelForState().modelFile(model).rotationX(180).addModel()
                .partialState().with(AmethystClusterBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()
                .partialState().with(AmethystClusterBlock.FACING, Direction.NORTH).modelForState().modelFile(model).rotationX(90).addModel()
                .partialState().with(AmethystClusterBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationX(90).rotationY(180).addModel()
                .partialState().with(AmethystClusterBlock.FACING, Direction.UP).modelForState().modelFile(model).addModel()
                .partialState().with(AmethystClusterBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();
    }
}