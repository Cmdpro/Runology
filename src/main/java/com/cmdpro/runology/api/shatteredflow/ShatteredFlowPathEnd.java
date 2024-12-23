package com.cmdpro.runology.api.shatteredflow;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ShatteredFlowPathEnd {
    public BlockPos entity;
    public BlockPos[] path;
    public ShatteredFlowPathEnd(BlockPos entity, BlockPos[] path) {
        this.entity = entity;
        this.path = path;
    }
}
