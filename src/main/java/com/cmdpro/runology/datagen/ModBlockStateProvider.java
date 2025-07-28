package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Runology.MODID, exFileHelper);
    }

    private static final int OTHERWORLDLY_DIRT_VARIANTS = 4;

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
        blockVariantsWithItem(BlockRegistry.OTHERWORLDLY_DIRT, OTHERWORLDLY_DIRT_VARIANTS);
        blockWithItem(BlockRegistry.OTHERWORLDLY_STONE);
        blockWithItem(BlockRegistry.OTHERWORLDLY_SAND);
        blockWithItem(BlockRegistry.OTHERWORLDLY_SANDSTONE);
        tallGrass(BlockRegistry.TALL_OTHERWORLDLY_GRASS);
        grass(BlockRegistry.SHORT_OTHERWORLDLY_GRASS);
    }
    private void blockVariantsWithItem(Supplier<Block> blockRegistryObject, int variants) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get());
        BlockModelBuilder itemModel = null;
        List<ConfiguredModel> models = new ArrayList<>();
        for (int i = 1; i <= variants; i++) {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_" + i);
            BlockModelBuilder model = models().cubeAll(loc.getPath() + "_" + i, texture);
            if (itemModel == null) {
                itemModel = model;
            }
            models.add(new ConfiguredModel(model));
        }
        getVariantBuilder(blockRegistryObject.get())
                .partialState().addModels(models.toArray(new ConfiguredModel[0]));
        if (itemModel != null) {
            simpleBlockItem(blockRegistryObject.get(), itemModel);
        }
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
        ResourceLocation top = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_top");
        BlockModelBuilder itemModel = null;
        List<ConfiguredModel> models = new ArrayList<>();
        for (int i = 1; i <= OTHERWORLDLY_DIRT_VARIANTS; i++) {
            ResourceLocation bottom = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/otherworldly_dirt" + "_" + i);
            ResourceLocation side = ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath() + "_side" + "_" + i);
            BlockModelBuilder model = models().withExistingParent(loc.getPath() + "_" + i, ModelProvider.BLOCK_FOLDER + "/cube")
                    .texture("west", side)
                    .texture("east", side)
                    .texture("north", side)
                    .texture("down", bottom)
                    .texture("up", top)
                    .texture("south", side)
                    .texture("particle", top);
            if (itemModel == null) {
                itemModel = model;
            }
            models.add(new ConfiguredModel(model));
        }
        getVariantBuilder(blockRegistryObject.get())
                .partialState().addModels(models.toArray(new ConfiguredModel[0]));
        simpleBlockItem(blockRegistryObject.get(), itemModel);
    }
}