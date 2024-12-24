package com.cmdpro.runology.api.shatteredflow;

import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
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
        List<BlockPos> toUpdateNetworks = new ArrayList<>();
        for (BlockPos i : getConnectedTo()) {
            BlockEntity entity = level.getBlockEntity(i);
            if (entity instanceof ShatteredFocusBlockEntity ent) {
                ent.connectedTo.remove(pos);
                toUpdateNetworks.add(i);
                ent.updateBlock();
            } else if (entity instanceof ShatteredRelayBlockEntity ent) {
                ent.connectedTo.remove(pos);
                toUpdateNetworks.add(i);
                ent.updateBlock();
            }
        }
        for (BlockPos i : toUpdateNetworks) {
            ShatteredFlowNetwork.updatePaths(level, i, new ShatteredFlowNetwork(new ArrayList<>(), new ArrayList<>()), new ArrayList<>());
        }
    }
    default void onPlace(Level level, BlockPos pos) {
        for (ShatteredRelayBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS)) {
            if (i.getBlockPos().getCenter().distanceTo(pos.getCenter()) <= 20) {
                if (!i.connectedTo.contains(pos)) {
                    i.connectedTo.add(pos);
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
                    i.updateBlock();
                }
                if (!getConnectedTo().contains(i.getBlockPos())) {
                    getConnectedTo().add(i.getBlockPos());
                }
            }
        }
        ShatteredFlowNetwork.updatePaths(level, pos, new ShatteredFlowNetwork(new ArrayList<>(), new ArrayList<>()), new ArrayList<>());
        BlockState blockState = level.getBlockState(pos);
        level.sendBlockUpdated(pos, blockState, blockState, 3);
        if (this instanceof BlockEntity ent) {
            ent.setChanged();
        }
    }
    default int tryUseEnergy(int amount, boolean cancelIfNotEnough) {
        if (cancelIfNotEnough) {
            int remaining = amount;
            for (BlockPos i : getNetwork().starts) {
                if (i instanceof ContainsShatteredFlow container) {
                    remaining -= container.getShatteredFlowStorage().amount;
                }
            }
            if (remaining > 0) {
                return remaining;
            }
        }
        int remaining = amount;
        for (BlockPos i : getNetwork().starts) {
            if (i instanceof ContainsShatteredFlow container) {
                if (remaining > 0) {
                    container.getShatteredFlowStorage().amount -= remaining;
                    remaining -= container.getShatteredFlowStorage().amount;
                } else {
                    break;
                }
            }
        }
        return Math.clamp(remaining, 0, Integer.MAX_VALUE);
    }
    default int tryUseEnergy(int amount) {
        return tryUseEnergy(amount, true);
    }
}
