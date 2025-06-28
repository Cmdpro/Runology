package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.CriteriaTriggerRegistry;
import com.cmdpro.runology.registry.SoundRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
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
            BlockPos.CODEC.listOf().fieldOf("starts").xmap((a) -> {
                if (a instanceof ArrayList<BlockPos> array) {
                    return array;
                }
                return new ArrayList<>(a);
            }, (a) -> (List<BlockPos>)a).forGetter((obj) -> new ArrayList<>(obj.starts)),
            BlockPos.CODEC.listOf().fieldOf("midpoints").xmap((a) -> {
                if (a instanceof ArrayList<BlockPos> array) {
                    return array;
                }
                return new ArrayList<>(a);
            }, (a) -> (List<BlockPos>)a).forGetter((obj) -> new ArrayList<>(obj.midpoints)),
            BlockPos.CODEC.listOf().fieldOf("ends").xmap((a) -> {
                if (a instanceof ArrayList<BlockPos> array) {
                    return array;
                }
                return new ArrayList<>(a);
            }, (a) -> (List<BlockPos>)a).forGetter((obj) -> new ArrayList<>(obj.ends)),
            BlockPos.CODEC.listOf().fieldOf("nodes").xmap((a) -> {
                if (a instanceof ArrayList<BlockPos> array) {
                    return array;
                }
                return new ArrayList<>(a);
            }, (a) -> (List<BlockPos>)a).forGetter((obj) -> new ArrayList<>(obj.nodes)),
            Codec.INT.fieldOf("surgeTime").forGetter((obj) -> obj.surgeTime)
    ).apply(instance, (uuid, starts, midpoints, ends, nodes, surgeTime) -> {
        ShatteredFlowNetwork network = new ShatteredFlowNetwork(UUID.fromString(uuid));
        network.starts = starts;
        network.midpoints = midpoints;
        network.ends = ends;
        network.nodes = nodes;
        network.surgeTime = surgeTime;
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
    public int surgeTime;
    public void connectToNetwork(Level level, BlockPos pos) {
        List<Map.Entry<BlockPos, ShatteredFlowNetwork>> toDisconnect = new ArrayList<>();
        connectToNetwork(level, pos, toDisconnect);
        for (Map.Entry<BlockPos, ShatteredFlowNetwork> i : toDisconnect) {
            i.getValue().disconnectFromNetwork(level, pos);
        }
    }
    public void startSurge(Level level, int time) {
        surgeTime = time;
        for (BlockPos i : nodes) {
            ChunkPos chunkPos = new ChunkPos(i);
            if (level.hasChunk(chunkPos.x, chunkPos.z)) {
                level.playSound(null, i, SoundRegistry.SHATTERED_FLOW_NETWORK_SURGE_START.value(), SoundSource.BLOCKS);
                for (Player j : level.players()) {
                    if (j.position().distanceTo(i.getCenter()) <= 30) {
                        CriteriaTriggerRegistry.NEARBY_SURGE.get().trigger((ServerPlayer)j);
                    }
                }
            }
        }
    }
    public void onSurgeEnd(Level level) {
        for (BlockPos i : nodes) {
            ChunkPos chunkPos = new ChunkPos(i);
            if (level.hasChunk(chunkPos.x, chunkPos.z)) {
                level.playSound(null, i, SoundRegistry.SHATTERED_FLOW_NETWORK_SURGE_END.value(), SoundSource.BLOCKS);
            }
        }
    }
    public void tick(Level level) {
        if (surgeTime > 0) {
            surgeTime--;
            if (surgeTime <= 0) {
                onSurgeEnd(level);
            }
        }
    }
    private void connectToNetwork(Level level, BlockPos pos, List<Map.Entry<BlockPos, ShatteredFlowNetwork>> toDisconnect) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShatteredRelayBlockEntity ent1) {
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
        } else if (blockEntity instanceof ShatteredFlowConnectable ent1) {
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
        } else if (blockEntity instanceof ShatteredFocusBlockEntity ent1) {
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
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShatteredRelayBlockEntity ent1) {
            if (ent1.path == this) {
                ent1.path = null;
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
        } else if (blockEntity instanceof ShatteredFlowConnectable ent1) {
            if (ent1.getNetwork() == this) {
                ent1.setNetwork(null);
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
            }
        } else if (blockEntity instanceof ShatteredFocusBlockEntity ent1) {
            if (ent1.path == this) {
                ent1.path = null;
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
        }
        starts.removeAll(toRemove);
        midpoints.removeAll(toRemove);
        ends.removeAll(toRemove);
        nodes.removeAll(toRemove);
        if (nodes.isEmpty()) {
            level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS).remove(this);
        }
    }
    private static List<BlockPos> searchForStarts(Level level, BlockPos searchFrom, List<BlockPos> starts, List<BlockPos> alreadyVisited) {
        if (alreadyVisited.contains(searchFrom)) {
            return starts;
        }
        alreadyVisited.add(searchFrom);
        BlockEntity blockEntity = level.getBlockEntity(searchFrom);
        if (blockEntity instanceof ShatteredRelayBlockEntity ent1) {
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                searchForStarts(level, i, starts, alreadyVisited);
            }
        } else if (blockEntity instanceof ShatteredFlowConnectable ent1) {
            for (BlockPos i : ent1.getConnectedTo().stream().toList()) {
                searchForStarts(level, i, starts, alreadyVisited);
            }
        } else if (blockEntity instanceof ShatteredFocusBlockEntity ent1) {
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
                if (j.getValue().nodes.isEmpty()) {
                    level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS).remove(j.getValue());
                }
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
        BlockEntity blockEntity = level.getBlockEntity(tryStart);
        if (blockEntity instanceof ShatteredRelayBlockEntity ent1) {
            if (!connectToNetwork.contains(tryStart)) {
                connectToNetwork.add(tryStart);
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                BlockEntity blockEntity2 = level.getBlockEntity(i);
                if (blockEntity2 instanceof ShatteredRelayBlockEntity || blockEntity2 instanceof ShatteredFocusBlockEntity || blockEntity2 instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, i, network, alreadyChecked, connectToNetwork);
                }
            }
        } else if (blockEntity instanceof ShatteredFlowConnectable ent1) {
            if (!connectToNetwork.contains(tryStart)) {
                connectToNetwork.add(tryStart);
            }
            for (BlockPos i : ent1.getConnectedTo().stream().toList()) {
                BlockEntity blockEntity2 = level.getBlockEntity(i);
                if (blockEntity2 instanceof ShatteredRelayBlockEntity || blockEntity2 instanceof ShatteredFocusBlockEntity || blockEntity2 instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, i, network, alreadyChecked, connectToNetwork);
                }
            }
        } else if (blockEntity instanceof ShatteredFocusBlockEntity ent1) {
            if (!connectToNetwork.contains(tryStart)) {
                connectToNetwork.add(tryStart);
            }
            for (BlockPos i : ent1.connectedTo.stream().toList()) {
                BlockEntity blockEntity2 = level.getBlockEntity(i);
                if (blockEntity2 instanceof ShatteredRelayBlockEntity || blockEntity2 instanceof ShatteredFocusBlockEntity || blockEntity2 instanceof ShatteredFlowConnectable) {
                    generateNetwork(level, blockEntity.getBlockPos(), network, alreadyChecked, connectToNetwork);
                }
            }
        }
    }
}
