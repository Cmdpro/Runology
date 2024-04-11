package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.Shatterleaf;
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
        blockWithItem(BlockInit.SHATTERSTONEBRICKS);
        blockWithItem(BlockInit.SHATTERSTONE);
        blockWithItem(BlockInit.CRACKEDSHATTERSTONEBRICKS);
        blockWithItem(BlockInit.CHISELEDSHATTERSTONEBRICKS);
        blockWithItem(BlockInit.AIRORE);
        blockWithItem(BlockInit.EARTHORE);
        blockWithItem(BlockInit.WATERORE);
        blockWithItem(BlockInit.FIREORE);
        blockWithItem(BlockInit.MYSTERIUMBLOCK);
        blockWithItem(BlockInit.MYSTERIUMORE);
        blockWithItem(BlockInit.PETRIFIEDSHATTERWOOD);
        blockWithItem(BlockInit.RAWMYSTERIUMBLOCK);
        blockWithItem(BlockInit.SHATTERCRYSTAL);
        blockWithItem(BlockInit.PRISMATICCRYSTAL);
        shatterleaf(BlockInit.SHATTERLEAF.get(), "shatterleaf", "shatterleaf");

        axisBlock((RotatedPillarBlock)BlockInit.SHATTERSTONEPILLAR.get(), new ResourceLocation(Runology.MOD_ID, "block/shatterstonepillar"), new ResourceLocation(Runology.MOD_ID, "block/shatterstonepillartop"));

        stairsBlock(((StairBlock) BlockInit.SHATTERSTONEBRICKSTAIRS.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));
        stairsBlock(((StairBlock) BlockInit.SHATTERSTONESTAIRS.get()), blockTexture(BlockInit.SHATTERSTONE.get()));

        slabBlock(((SlabBlock) BlockInit.SHATTERSTONESLAB.get()), blockTexture(BlockInit.SHATTERSTONE.get()), blockTexture(BlockInit.SHATTERSTONE.get()));
        slabBlock(((SlabBlock) BlockInit.SHATTERSTONEBRICKSLAB.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));

        wallBlock(((WallBlock) BlockInit.SHATTERSTONEBRICKWALL.get()), blockTexture(BlockInit.SHATTERSTONEBRICKS.get()));
        wallBlock(((WallBlock) BlockInit.SHATTERSTONEWALL.get()), blockTexture(BlockInit.SHATTERSTONE.get()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    public void shatterleaf(Block block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> shatterleafStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] shatterleafStates(BlockState state, Block block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().cross(modelName + state.getValue(((Shatterleaf) block).AGE),
                new ResourceLocation(Runology.MOD_ID, "block/" + textureName + (state.getValue(((Shatterleaf) block).AGE) == 3 ? "" : "empty"))).renderType("cutout"));

        return models;
    }
}