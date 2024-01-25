package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Runology.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SAPPHIRE_BLOCK);
        blockWithItem(ModBlocks.RAW_SAPPHIRE_BLOCK);

        blockWithItem(ModBlocks.SAPPHIRE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SAPPHIRE_ORE);
        blockWithItem(ModBlocks.END_STONE_SAPPHIRE_ORE);
        blockWithItem(ModBlocks.NETHER_SAPPHIRE_ORE);

        blockWithItem(ModBlocks.SOUND_BLOCK);

        axisBlock((RotatedPillarBlock)BlockInit.SHATTERSTONEPILLAR.get(), new ResourceLocation(Runology.MOD_ID, "block/shatterstonepillar"), new ResourceLocation(Runology.MOD_ID, "block/shatterstonepillartop"));

        stairsBlock(((StairBlock) BlockInit.SHATTERSTONEBRICKSLAB.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));
        stairsBlock(((StairBlock) BlockInit.SHATTERSTONESLAB.get()), blockTexture(BlockInit.SHATTERSTONE.get()));

        slabBlock(((SlabBlock) BlockInit.SHATTERSTONESTAIRS.get()), blockTexture(BlockInit.SHATTERSTONE.get()), blockTexture(BlockInit.SHATTERSTONE.get()));
        slabBlock(((SlabBlock) BlockInit.SHATTERSTONEBRICKSTAIRS.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));

        wallBlock(((WallBlock) BlockInit.SHATTERSTONEBRICKWALL.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));
        wallBlock(((WallBlock) BlockInit.SHATTERSTONEWALL.get()), blockTexture(BlockInit.SHATTERSTONE.get()));

        makeStrawberryCrop((CropBlock) ModBlocks.STRAWBERRY_CROP.get(), "strawberry_stage", "strawberry_stage");
    }


    public void makeStrawberryCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> strawberryStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] strawberryStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()),
                new ResourceLocation(TutorialMod.MOD_ID, "block/" + textureName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeCornCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cornStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cornStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CornCropBlock) block).getAgeProperty()),
                new ResourceLocation(TutorialMod.MOD_ID, "block/" + textureName + state.getValue(((CornCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}