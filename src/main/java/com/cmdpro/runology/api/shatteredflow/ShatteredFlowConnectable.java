package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShatteredFlowConnectable {
    default Vec3 getConnectOffset() {
        return Vec3.ZERO;
    }
    List<BlockPos> getConnectedTo();
    void setNetwork(ShatteredFlowNetwork network);
    ShatteredFlowNetwork getNetwork();
    default void onRemove(Level level, BlockPos pos, boolean isNew) {
        if (!level.isClientSide) {
            level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).remove(this);
            if (isNew) {
                if (getNetwork() != null) {
                    getNetwork().disconnectFromNetwork(level, pos);
                }
                for (BlockPos i : getConnectedTo()) {
                    BlockEntity ent = level.getBlockEntity(i);
                    if (ent instanceof ShatteredFlowConnectable ent2) {
                        ent2.getConnectedTo().remove(pos);
                        updateBlock(ent);
                    }
                }
                getConnectedTo().clear();
            }
        }
    }
    default void onLoad(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            var connectable = level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES);
            if (!level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).contains(this)) {
                level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).add(this);
            }
            for (ShatteredFlowConnectable i : level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES)) {
                if (i instanceof BlockEntity entity && (entity instanceof ShatteredRelayBlockEntity || entity instanceof ShatteredFocusBlockEntity)) {
                    if (entity.getBlockPos().getCenter().distanceTo(pos.getCenter()) <= 20) {
                        if (!getConnectedTo().contains(entity.getBlockPos())) {
                            getConnectedTo().add(entity.getBlockPos());
                        }
                        if (!i.getConnectedTo().contains(pos)) {
                            i.getConnectedTo().add(pos);
                            if (i.getNetwork() != null) {
                                i.getNetwork().connectToNetwork(level, pos);
                            } else if (getNetwork() != null) {
                                getNetwork().connectToNetwork(level, entity.getBlockPos());
                            }
                            updateBlock(entity);
                        }
                    }
                }
            }
            BlockState blockState = level.getBlockState(pos);
            level.sendBlockUpdated(pos, blockState, blockState, 3);
            if (this instanceof BlockEntity ent) {
                ent.setChanged();
            }
        }
    }
    static void updateBlock(BlockEntity entity) {
        if (entity.hasLevel()) {
            BlockState blockState = entity.getLevel().getBlockState(entity.getBlockPos());
            entity.getLevel().sendBlockUpdated(entity.getBlockPos(), blockState, blockState, 3);
            entity.setChanged();
        }
    }
    default void addToTag(Level level, CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos i : getConnectedTo()) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
        if (getNetwork() != null) {
            tag.putUUID("network", getNetwork().uuid);
            tag.putString("networkLevel", level.dimension().location().toString());
        }
    }
    default void getFromTag(BlockPos pos, CompoundTag tag, HolderLookup.Provider registryAccess) {
        getConnectedTo().clear();
        if (tag.contains("link")) {
            ListTag list = (ListTag) tag.get("link");
            for (Tag i : list) {
                CompoundTag blockpos = (CompoundTag) i;
                getConnectedTo().add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
            }
        }
        if (tag.contains("network") && tag.contains("networkLevel")) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                Level level = ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("networkLevel"))));
                if (level != null) {
                    if (getNetwork() != null) {
                        getNetwork().ends.remove(pos);
                        getNetwork().midpoints.remove(pos);
                        getNetwork().starts.remove(pos);
                        getNetwork().nodes.remove(pos);
                        if (getNetwork().nodes.isEmpty()) {
                            level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS).remove(getNetwork());
                        }
                    }
                    setNetwork(RunologyUtil.getShatteredFlowNetworkFromUUID(level, tag.getUUID("network")));
                }
            }
        }
    }
    default int tryUseEnergy(Level level, int amount, boolean cancelIfNotEnough) {
        if (getNetwork() == null) {
            return amount;
        }
        if (cancelIfNotEnough) {
            int remaining = amount;
            for (BlockPos i : getNetwork().starts) {
                if (level.getBlockEntity(i) instanceof ContainsShatteredFlow container) {
                    remaining -= container.getShatteredFlowStorage().amount;
                }
            }
            if (remaining > 0) {
                return remaining;
            }
        }
        int remaining = amount;
        for (BlockPos i : getNetwork().starts) {
            if (level.getBlockEntity(i) instanceof ContainsShatteredFlow container) {
                if (remaining > 0) {
                    int amount2 = container.getShatteredFlowStorage().amount;
                    container.getShatteredFlowStorage().amount = Math.clamp(container.getShatteredFlowStorage().amount-remaining, 0, Integer.MAX_VALUE);
                    remaining -= amount2;
                } else {
                    break;
                }
            }
        }
        return Math.clamp(remaining, 0, Integer.MAX_VALUE);
    }
    default int hasEnergy(Level level, int amount) {
        if (getNetwork() == null) {
            return amount;
        }
        int remaining = amount;
        for (BlockPos i : getNetwork().starts) {
            if (level.getBlockEntity(i) instanceof ContainsShatteredFlow container) {
                remaining -= container.getShatteredFlowStorage().amount;
            }
        }
        return Math.max(remaining, 0);
    }
    default int getEnergy(Level level) {
        if (getNetwork() == null) {
            return 0;
        }
        int energy = 0;
        for (BlockPos i : getNetwork().starts) {
            if (level.getBlockEntity(i) instanceof ContainsShatteredFlow container) {
                energy += container.getShatteredFlowStorage().amount;
            }
        }
        return energy;
    }
    default int tryUseEnergy(Level level, int amount) {
        return tryUseEnergy(level, amount, true);
    }
}
