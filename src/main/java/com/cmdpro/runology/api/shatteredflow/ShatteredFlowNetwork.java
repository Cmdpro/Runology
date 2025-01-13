package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShatteredFlowNetwork {
    public static Codec<ShatteredFlowNetwork> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("uuid").forGetter((obj) -> obj.uuid.toString()),
            BlockPos.CODEC.listOf().fieldOf("starts").forGetter((obj) -> obj.starts),
            BlockPos.CODEC.listOf().fieldOf("midpoints").forGetter((obj) -> obj.midpoints),
            BlockPos.CODEC.listOf().fieldOf("ends").forGetter((obj) -> obj.ends),
            BlockPos.CODEC.listOf().fieldOf("nodes").forGetter((obj) -> obj.nodes)
    ).apply(instance, (uuid, starts, midpoints, ends, nodes) -> {
        ShatteredFlowNetwork network = new ShatteredFlowNetwork(UUID.fromString(uuid));
        network.starts = starts;
        network.midpoints = midpoints;
        network.ends = ends;
        network.nodes = nodes;
        return network;
    }));
    public ShatteredFlowNetwork() {
        uuid = UUID.randomUUID();
    }
    public ShatteredFlowNetwork(UUID uuid) {
        this.uuid = uuid;
    }
    public UUID uuid;
    public List<BlockPos> starts = new ArrayList<>();
    public List<BlockPos> midpoints = new ArrayList<>();
    public List<BlockPos> ends = new ArrayList<>();
    public List<BlockPos> nodes = new ArrayList<>();
    public void connectToNetwork(Level level, BlockPos pos) {
        List<Map.Entry<BlockPos, ShatteredFlowNetwork>> toDisconnect = new ArrayList<>();
        connectToNetwork(level, pos, toDisconnect);
        for (Map.Entry<BlockPos, ShatteredFlowNetwork> i : toDisconnect) {
            if (level.getBlockEntity(i.getKey()) instanceof ShatteredRelayBlockEntity ent1) {
                i.getValue().disconnectFromNetwork(level, pos);
            } else if (level.getBlockEntity(i.getKey()) instanceof ShatteredFocusBlockEntity ent1) {
                i.getValue().disconnectFromNetwork(level, pos);
            } else if (level.getBlockEntity(i.getKey()) instanceof ShatteredFlowConnectable ent1) {
                i.getValue().disconnectFromNetwork(level, pos);
            }
        }
    }
    private void connectToNetwork(Level level, BlockPos pos, List<Map.Entry<BlockPos, ShatteredFlowNetwork>> toDisconnect) {
        if (level.getBlockEntity(pos) instanceof ShatteredRelayBlockEntity ent1) {
            if (ent1.path == this) {
                return;
            } else if (ent1.path != null) {
                toDisconnect.add(Map.entry(pos, ent1.path));
            }
            ent1.path = this;
            if (!midpoints.contains(pos)) {
                midpoints.add(pos);
            }
            if (!nodes.contains(pos)) {
                nodes.add(pos);
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                connectToNetwork(level, i, toDisconnect);
            }
            ent1.updateBlock();
        } else if (level.getBlockEntity(pos) instanceof ShatteredFlowConnectable ent1) {
            if (ent1.getNetwork() == this) {
                return;
            } else if (ent1.getNetwork() != null) {
                toDisconnect.add(Map.entry(pos, ent1.getNetwork()));
            }
            ent1.setNetwork(this);
            if (!ends.contains(pos)) {
                ends.add(pos);
            }
            if (!nodes.contains(pos)) {
                nodes.add(pos);
            }
            BlockState blockState = level.getBlockState(pos);
            level.sendBlockUpdated(pos, blockState, blockState, 3);
            ((BlockEntity)ent1).setChanged();
        } else if (level.getBlockEntity(pos) instanceof ShatteredFocusBlockEntity ent1) {
            if (ent1.path == this) {
                return;
            } else if (ent1.path != null) {
                toDisconnect.add(Map.entry(pos, ent1.path));
            }
            ent1.path = this;
            if (!starts.contains(pos)) {
                starts.add(pos);
            }
            if (!nodes.contains(pos)) {
                nodes.add(pos);
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                connectToNetwork(level, i, toDisconnect);
            }
            ent1.updateBlock();
        }
    }
    public void disconnectFromNetwork(Level level, BlockPos pos) {
        starts.remove(pos);
        midpoints.remove(pos);
        ends.remove(pos);
        nodes.remove(pos);
        List<BlockPos> toRemove = new ArrayList<>();
        if (level.getBlockEntity(pos) instanceof ShatteredRelayBlockEntity ent1) {
            if (ent1.path == this) {
                ent1.path = null;
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                } else if (level.getBlockEntity(i) instanceof ShatteredFlowConnectable ent2) {
                    ent2.getConnectedTo().remove(pos);
                    BlockState blockState = level.getBlockState(pos);
                    level.sendBlockUpdated(pos, blockState, blockState, 3);
                    ((BlockEntity)ent2).setChanged();
                } else if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                }
                List<BlockPos> starts = searchForStarts(level, i, new ArrayList<>(), new ArrayList<>());
                if (starts.size() < this.starts.size()) {
                    toRemove.addAll(generateNetwork(level, i).nodes);
                }
            }
        } else if (level.getBlockEntity(pos) instanceof ShatteredFlowConnectable ent1) {
            if (ent1.getNetwork() == this) {
                ent1.setNetwork(null);
            }
            for (BlockPos i : ent1.getConnectedTo()) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                } else if (level.getBlockEntity(i) instanceof ShatteredFlowConnectable ent2) {
                    ent2.getConnectedTo().remove(pos);
                    BlockState blockState = level.getBlockState(pos);
                    level.sendBlockUpdated(pos, blockState, blockState, 3);
                    ((BlockEntity)ent2).setChanged();
                } else if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                }
                List<BlockPos> starts = searchForStarts(level, i, new ArrayList<>(), new ArrayList<>());
                if (starts.size() < this.starts.size()) {
                    toRemove.addAll(generateNetwork(level, i).nodes);
                }
            }
        } else if (level.getBlockEntity(pos) instanceof ShatteredFocusBlockEntity ent1) {
            if (ent1.path == this) {
                ent1.path = null;
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                } else if (level.getBlockEntity(i) instanceof ShatteredFlowConnectable ent2) {
                    ent2.getConnectedTo().remove(pos);
                    BlockState blockState = level.getBlockState(pos);
                    level.sendBlockUpdated(pos, blockState, blockState, 3);
                    ((BlockEntity)ent2).setChanged();
                } else if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent2) {
                    ent2.connectedTo.remove(pos);
                    ent2.updateBlock();
                }
                List<BlockPos> starts = searchForStarts(level, i, new ArrayList<>(), new ArrayList<>());
                if (starts.size() < this.starts.size()) {
                    toRemove.addAll(generateNetwork(level, i).nodes);
                }
            }
        }
        starts.removeAll(toRemove);
        midpoints.removeAll(toRemove);
        ends.removeAll(toRemove);
        nodes.removeAll(toRemove);
    }
    private static List<BlockPos> searchForStarts(Level level, BlockPos searchFrom, List<BlockPos> starts, List<BlockPos> alreadyVisited) {
        if (alreadyVisited.contains(searchFrom)) {
            return starts;
        }
        alreadyVisited.add(searchFrom);
        if (level.getBlockEntity(searchFrom) instanceof ShatteredRelayBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                searchForStarts(level, i, starts, alreadyVisited);
            }
        } else if (level.getBlockEntity(searchFrom) instanceof ShatteredFlowConnectable ent1) {
            for (BlockPos i : ent1.getConnectedTo().stream().toList()) {
                searchForStarts(level, i, starts, alreadyVisited);
            }
        } else if (level.getBlockEntity(searchFrom) instanceof ShatteredFocusBlockEntity ent1) {
            starts.add(searchFrom);
        }
        return starts;
    }
    public static ShatteredFlowNetwork generateNetwork(Level level, BlockPos tryStart) {
        ShatteredFlowNetwork network = new ShatteredFlowNetwork();
        List<BlockPos> connectToNetwork = new ArrayList<>();
        generateNetwork(level, tryStart, network, new ArrayList<>(), connectToNetwork);
        for (BlockPos i : connectToNetwork) {
            List<Map.Entry<BlockPos, ShatteredFlowNetwork>> toDisconnect = new ArrayList<>();
            network.connectToNetwork(level, i, toDisconnect);
            for (var j : toDisconnect) {
                j.getValue().nodes.remove(j.getKey());
                j.getValue().starts.remove(j.getKey());
                j.getValue().midpoints.remove(j.getKey());
                j.getValue().ends.remove(j.getKey());
            }
        }
        level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS).add(network);
        return network;
    }
    private static void generateNetwork(Level level, BlockPos tryStart, ShatteredFlowNetwork network, List<BlockPos> alreadyChecked, List<BlockPos> connectToNetwork) {
        if (alreadyChecked.contains(tryStart)) {
            return;
        }
        alreadyChecked.add(tryStart);
        if (level.getBlockEntity(tryStart) instanceof ShatteredRelayBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                BlockEntity blockEntity = level.getBlockEntity(i);
                if (blockEntity instanceof ShatteredRelayBlockEntity || blockEntity instanceof ShatteredFocusBlockEntity || blockEntity instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, i, network, alreadyChecked, connectToNetwork);
                }
                if (!connectToNetwork.contains(i)) {
                    connectToNetwork.add(i);
                }
            }
        } else if (level.getBlockEntity(tryStart) instanceof ShatteredFlowConnectable ent1) {
            for (BlockPos i : ent1.getConnectedTo().stream().toList()) {
                BlockEntity blockEntity = level.getBlockEntity(i);
                if (blockEntity instanceof ShatteredRelayBlockEntity || blockEntity instanceof ShatteredFocusBlockEntity || blockEntity instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, i, network, alreadyChecked, connectToNetwork);
                }
                if (!connectToNetwork.contains(i)) {
                    connectToNetwork.add(i);
                }
            }
        } else if (level.getBlockEntity(tryStart) instanceof ShatteredFocusBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                BlockEntity blockEntity = level.getBlockEntity(i);
                if (blockEntity instanceof ShatteredRelayBlockEntity || blockEntity instanceof ShatteredFocusBlockEntity || blockEntity instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, blockEntity.getBlockPos(), network, alreadyChecked, connectToNetwork);
                }
                if (!connectToNetwork.contains(i)) {
                    connectToNetwork.add(i);
                }
            }
        }
    }
}
