package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public interface ShatteredFlowConnectable {
    default Vec3 getConnectOffset() {
        return Vec3.ZERO;
    }
    List<BlockPos> getConnectedTo();
    void setNetwork(ShatteredFlowNetwork network);
    ShatteredFlowNetwork getNetwork();
    default void onRemove(Level level, BlockPos pos) {
        if (getNetwork() != null) {
            getNetwork().disconnectFromNetwork(level, pos);
        }
        for (BlockPos i : getConnectedTo()) {
            if (level.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                ent2.connectedTo.remove(pos);
                ent2.updateBlock();
            }
            if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent2) {
                ent2.connectedTo.remove(pos);
                ent2.updateBlock();
            }
        }
        getConnectedTo().clear();
    }
    default void onLoad(Level level, BlockPos pos) {
        for (ShatteredRelayBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS)) {
            if (i.getBlockPos().getCenter().distanceTo(pos.getCenter()) <= 20) {
                if (!i.connectedTo.contains(pos)) {
                    i.connectedTo.add(pos);
                    if (i.path != null) {
                        i.path.connectToNetwork(level, pos);
                    }
                    i.updateBlock();
                }
                if (!getConnectedTo().contains(i.getBlockPos())) {
                    getConnectedTo().add(i.getBlockPos());
                }
            }
        }
        for (ShatteredFocusBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES)) {
            if (i.getBlockPos().getCenter().distanceTo(pos.getCenter()) <= 20) {
                if (!i.connectedTo.contains(pos)) {
                    i.connectedTo.add(pos);
                    if (i.path != null) {
                        i.path.connectToNetwork(level, pos);
                    }
                    i.updateBlock();
                }
                if (!getConnectedTo().contains(i.getBlockPos())) {
                    getConnectedTo().add(i.getBlockPos());
                }
            }
        }
        BlockState blockState = level.getBlockState(pos);
        level.sendBlockUpdated(pos, blockState, blockState, 3);
        if (this instanceof BlockEntity ent) {
            ent.setChanged();
        }
    }
    default void addToTag(CompoundTag tag) {
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
        }
    }
    default void getFromTag(Level level, CompoundTag tag) {
        getConnectedTo().clear();
        if (tag.contains("link")) {
            ListTag list = (ListTag) tag.get("link");
            for (Tag i : list) {
                CompoundTag blockpos = (CompoundTag) i;
                getConnectedTo().add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
            }
        }
        if (tag.contains("network")) {
            setNetwork(RunologyUtil.getShatteredFlowNetworkFromUUID(level, tag.getUUID("network")));
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
    default int tryUseEnergy(Level level, int amount) {
        return tryUseEnergy(level, amount, true);
    }
}
