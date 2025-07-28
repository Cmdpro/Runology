package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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

        grassBlock(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK);
        blockWithItem(BlockRegistry.OTHERWORLDLY_DIRT);
        blockWithItem(BlockRegistry.OTHERWORLDLY_STONE);
        blockWithItem(BlockRegistry.OTHERWORLDLY_SAND);
        blockWithItem(BlockRegistry.OTHERWORLDLY_SANDSTONE);
        tallGrass(BlockRegistry.TALL_OTHERWORLDLY_GRASS);
        grass(BlockRegistry.SHORT_OTHERWORLDLY_GRASS);
    }
    private void tallGrass(Supplier<Block> blockRegistryObject) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get());
        ResourceLocation lower = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_lower");
        ResourceLocation upper = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_upper");
        BlockModelBuilder lowerModel = models().cross(loc.getPath() + "_lower", lower);
        lowerModel.renderType("cutout");
        BlockModelBuilder upperModel = models().cross(loc.getPath() + "_upper", upper);
        upperModel.renderType("cutout");
        getVariantBuilder(blockRegistryObject.get())
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).modelForState().modelFile(lowerModel).addModel()
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).modelForState().modelFile(upperModel).addModel();
        itemModels().withExistingParent(loc.getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                upper);
    }
    private void grass(Supplier<Block> blockRegistryObject) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get());
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath());
        BlockModelBuilder model = models().cross(loc.getPath(), texture);
        model.renderType("cutout");
        simpleBlock(blockRegistryObject.get(), model);
        itemModels().withExistingParent(loc.getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                texture);
    }

    private void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void grassBlock(Supplier<Block> blockRegistryObject) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get());
        ResourceLocation side = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_side");
        ResourceLocation top = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_top");
        ResourceLocation bottom = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/otherworldly_dirt");
        BlockModelBuilder model = models().withExistingParent(loc.getPath(), ModelProvider.BLOCK_FOLDER + "/cube")
                .texture("west", side)
                .texture("east", side)
                .texture("north", side)
                .texture("down", bottom)
                .texture("up", top)
                .texture("south", side)
                .texture("particle", top);
        simpleBlock(blockRegistryObject.get(), model);
        simpleBlockItem(blockRegistryObject.get(), model);
    }
}