package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ShatteredFlowNetwork {
    public List<BlockPos> starts;
    public ShatteredFlowNetwork(List<BlockPos> starts) {
        this.starts = starts;
    }
    public static void updatePaths(Level level, BlockPos tryStart, ShatteredFlowNetwork network, List<BlockPos> alreadyChecked) {
        if (alreadyChecked.contains(tryStart)) {
            return;
        }
        alreadyChecked.add(tryStart);
        if (level.getBlockEntity(tryStart) instanceof ShatteredRelayBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity || level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity) {
                    updatePaths(level, i, network, alreadyChecked);
                }
            }
            ent1.path = network;
        } else if (level.getBlockEntity(tryStart) instanceof ShatteredFocusBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent) {
                    updatePaths(level, ent.getBlockPos(), network, alreadyChecked);
                }
            }
            if (!network.starts.contains(tryStart)) {
                network.starts.add(tryStart);
            }
            ent1.path = network;
        }
    }
}
