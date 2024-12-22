package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ShatteredFlowNetwork {
    public List<BlockPos> starts;
    public List<ShatteredFlowPathEnd> ends;
    public ShatteredFlowNetwork(List<BlockPos> starts, List<ShatteredFlowPathEnd> ends) {
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
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent) {
                    updatePaths(level, ent.getBlockPos(), network, alreadyChecked);
                }
            }
            ent1.path = network;
        } else if (level.getBlockEntity(tryStart) instanceof ShatteredFocusBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent) {
                    updatePaths(level, ent.getBlockPos(), network, alreadyChecked);
                }
            }
            network.ends.addAll(calculate(ent1).stream().filter((a) -> !network.ends.stream().anyMatch((b) -> b.entity == a.entity)).toList());
            ent1.path = network;
        }
    }
    public static List<ShatteredFlowPathEnd> calculate(ShatteredFocusBlockEntity start) {
        List<BlockPos> alreadyVisited = new ArrayList<>();
        BlockPos node = start.getBlockPos();
        alreadyVisited.add(node);
        List<ShatteredFlowPathEnd> ends = getEnds(start.getLevel(), node, alreadyVisited);
        return ends;
    }
    public static List<ShatteredFlowPathEnd> getEnds(Level level, BlockPos node, List<BlockPos> alreadyVisited) {
        List<ShatteredFlowPathEnd> ends = new ArrayList<>();
        List<BlockPos> connected = null;
        if (level.getBlockEntity(node) instanceof ShatteredFocusBlockEntity ent) {
            connected = ent.connectedTo;
        } else if (level.getBlockEntity(node) instanceof ShatteredRelayBlockEntity ent) {
            connected = ent.connectedTo;
        }
        if (connected == null) {
            return new ArrayList<>();
        }
        for (BlockPos i : connected) {
            if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent) {
                if (!alreadyVisited.contains(ent)) {
                    alreadyVisited.add(ent.getBlockPos());
                    if (ent.connectedTo.isEmpty()) {
                        ends.add(new ShatteredFlowPathEnd(ent.getBlockPos(), alreadyVisited.toArray(new BlockPos[0])));
                    } else {
                        ends.addAll(getEnds(ent.getLevel(), ent.getBlockPos(), alreadyVisited));
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return ends;
    }
}
