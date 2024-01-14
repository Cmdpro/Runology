package com.cmdpro.runology.worldgen.features;

import com.cmdpro.runology.init.BlockInit;
import com.mojang.serialization.Codec;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.*;

import java.util.ArrayList;
import java.util.List;

public class ShatterTree extends Feature {
    public ShatterTree(Codec pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext pContext) {
        boolean reachedBottom = false;
        BlockPos pos = pContext.origin();
        if (!pContext.level().isEmptyBlock(pos)) {
            return false;
        }
        for (int i = 0; i < 64; i++) {
            if (!pContext.level().isEmptyBlock(pContext.origin().offset(0, -i, 0))) {
                if (!pContext.level().getBlockState(pContext.origin().offset(0, -i, 0)).is(BlockInit.SHATTERSTONE.get())) {
                    reachedBottom = false;
                    break;
                }
                reachedBottom = true;
                pos = pContext.origin().offset(0, (-i)+1, 0);
                break;
            }
        }
        if (!reachedBottom) {
            return false;
        }
        int height = pContext.random().nextInt(7, 10);
        for (int i = 0; i < height; i++) {
            this.setBlock(pContext.level(), pos.offset(0, i, 0), BlockInit.SHATTERSTONE.get().defaultBlockState());
        }
        int branchAmount = pContext.random().nextInt(4, height-2);
        int dir = pContext.random().nextInt(0, 360);
        for (int i = 0; i < branchAmount; i++) {
            dir += pContext.random().nextInt(35, 90);
            int length = pContext.random().nextInt(6, 10);
            int branchHeight = pContext.random().nextInt(3, height);
            for (int o = 0; o < length; o++) {
                this.setBlock(pContext.level(), pos.offset((int)(Math.sin(dir)*o), branchHeight+(o/2), (int)(Math.cos(dir)*o)), BlockInit.SHATTERSTONE.get().defaultBlockState());
            }
        }
        return true;
    }
}
