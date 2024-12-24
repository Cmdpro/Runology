package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ShatteredFlowNetwork {
    public List<BlockPos> starts;
    public List<BlockPos> ends;
    public ShatteredFlowNetwork(List<BlockPos> starts, List<BlockPos> ends) {
        this.starts = starts;
        this.ends = ends;
    }
    public static void updatePaths(Level level, BlockPos tryStart, ShatteredFlowNetwork network, List<BlockPos> alreadyChecked) {
        if (alreadyChecked.contains(tryStart)) {
            return;
        }
        alreadyChecked.add(tryStart);
        if (level.getBlockEntity(tryStart) instanceof ShatteredRelayBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo) {
                BlockEntity blockEntity = level.getBlockEntity(i);
                if (blockEntity instanceof ShatteredRelayBlockEntity || blockEntity instanceof ShatteredFocusBlockEntity) {
                    updatePaths(level, i, network, alreadyChecked);
                } else if (blockEntity instanceof ContainsShatteredFlow) {
                    if (!network.ends.contains(blockEntity.getBlockPos())) {
                        network.ends.add(blockEntity.getBlockPos());
                    }
                }
            }
            ent1.path = network;
        } else if (level.getBlockEntity(tryStart) instanceof ShatteredFocusBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo) {
                BlockEntity blockEntity = level.getBlockEntity(i);
                if (blockEntity instanceof ShatteredRelayBlockEntity ent) {
                    updatePaths(level, ent.getBlockPos(), network, alreadyChecked);
                } else if (blockEntity instanceof ShatteredFlowConnectable containsShatteredFlow) {
                    if (!network.ends.contains(blockEntity.getBlockPos())) {
                        network.ends.add(blockEntity.getBlockPos());
                    }
                    containsShatteredFlow.setNetwork(network);
                }
            }
            if (!network.starts.contains(tryStart)) {
                network.starts.add(tryStart);
            }
            ent1.path = network;
        }
    }
}
