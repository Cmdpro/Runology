package com.cmdpro.runology.worldgen.features;

import com.cmdpro.runology.init.BlockInit;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShatterTree extends Feature<NoneFeatureConfiguration> {
    public ShatterTree(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        boolean reachedBottom = false;
        BlockPos pos = pContext.origin();
        if (!pContext.level().isEmptyBlock(pos)) {
            return false;
        }
        for (int i = 0; i < 64; i++) {
            if (pContext.level().getBlockState(pContext.origin().offset(0, -i, 0)).is(BlockInit.SHATTERSTONE.get())) {
                reachedBottom = true;
                pos = pContext.origin().offset(0, (-i)+1, 0);
                break;
            } else if (pContext.level().getBlockState(pContext.origin().offset(0, -i, 0)).isSolid()) {
                break;
            }
        }
        if (!reachedBottom) {
            return false;
        }
        int height = pContext.random().nextInt(7, 10);
        for (int i = 0; i < height; i++) {
            this.setBlock(pContext.level(), pos.offset(0, i, 0), BlockInit.PETRIFIEDSHATTERWOOD.get().defaultBlockState());
        }
        int branchAmount = pContext.random().nextInt(4, height-2);
        List<Integer> branchesList = new ArrayList<>();
        for (int i = 0; i < branchAmount; i++) {
            branchesList.add(pContext.random().nextInt(3, height));
        }
        Integer[] branches = branchesList.toArray(new Integer[] {});
        Arrays.sort(branches);
        int dir = pContext.random().nextInt(0, 360);
        for (int i : branches) {
            int length = pContext.random().nextInt(6, 10);
            float yoffset = 0;
            for (int o = 0; o < length; o++) {
                yoffset += 0.5f+(0.5*(o/length));
                this.setBlock(pContext.level(), pos.offset((int)(Math.sin(dir)*o), i+(int)yoffset, (int)(Math.cos(dir)*o)), BlockInit.PETRIFIEDSHATTERWOOD.get().defaultBlockState());
            }
            this.setBlock(pContext.level(), pos.offset((int)(Math.sin(dir)*length), i+1+(int)yoffset, (int)(Math.cos(dir)*length)), BlockInit.PETRIFIEDSHATTERWOOD.get().defaultBlockState());
            dir += pContext.random().nextInt(45, 60);
        }
        return true;
    }
}
