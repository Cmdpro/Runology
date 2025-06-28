package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowConnectable;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShatteredRelayBlockEntity extends BlockEntity implements ShatteredFlowConnectable {
    public ShatteredRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTERED_RELAY.get(), pos, state);
        connectedTo = new ArrayList<>();
    }
    public ShatteredFlowNetwork path;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        ListTag list = new ListTag();
        for (BlockPos i : connectedTo) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
        if (path != null) {
            tag.putUUID("network", path.uuid);
            tag.putString("networkLevel", level.dimension().location().toString());
        }
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        connectedTo.clear();
        if (tag.contains("link")) {
            ListTag list = (ListTag) tag.get("link");
            for (Tag i : list) {
                CompoundTag blockpos = (CompoundTag) i;
                connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
            }
        }
        if (tag.contains("network") && tag.contains("networkLevel")) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                Level level = ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("networkLevel"))));
                if (level != null) {
                    if (path != null) {
                        path.ends.remove(getBlockPos());
                        path.midpoints.remove(getBlockPos());
                        path.starts.remove(getBlockPos());
                        path.nodes.remove(getBlockPos());
                        if (path.nodes.isEmpty()) {
                            level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS).remove(path);
                        }
                    }
                    path = RunologyUtil.getShatteredFlowNetworkFromUUID(level, tag.getUUID("network"));
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            if (!level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).contains(this)) {
                level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).add(this);
            }
            for (ShatteredFlowConnectable i : level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES)) {
                if (i == this) {
                    continue;
                }
                if (i instanceof BlockEntity entity) {
                    if (entity.getBlockPos().getCenter().distanceTo(getBlockPos().getCenter()) <= 20) {
                        if (!this.connectedTo.contains(entity.getBlockPos())) {
                            this.connectedTo.add(entity.getBlockPos());
                        }
                        if (!i.getConnectedTo().contains(getBlockPos())) {
                            i.getConnectedTo().add(getBlockPos());
                            if (i.getNetwork() != null) {
                                i.getNetwork().connectToNetwork(level, getBlockPos());
                            } else if (path != null) {
                                path.connectToNetwork(level, entity.getBlockPos());
                            }
                            ShatteredFlowConnectable.updateBlock(entity);
                        }
                    }
                }
            }
            updateBlock();
        }
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    public List<BlockPos> connectedTo;
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide) {
            if (isPowered) {
                pLevel.addParticle(ParticleRegistry.SHATTER.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1);
            }
        } else {
            boolean powered = false;
            if (path != null) {
                for (BlockPos i : path.starts) {
                    if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent) {
                        if (ent.storage.amount > 0) {
                            powered = true;
                            break;
                        }
                    }
                }
            }
            if (isPowered != powered) {
                updateBlock();
            }
            isPowered = powered;
        }
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (!pkt.getTag().isEmpty()) {
            decodeUpdateTag(pkt.getTag(), lookupProvider);
        }
    }
    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        decodeUpdateTag(tag, lookupProvider);
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ListTag list = (ListTag)tag.get("link");
        connectedTo.clear();
        for (Tag i : list) {
            CompoundTag blockpos = (CompoundTag)i;
            connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
        }
        isPowered = tag.getBoolean("isPowered");
    }
    public boolean isPowered;
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (BlockPos i : connectedTo) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
        tag.putBoolean("isPowered", isPowered);
        return tag;
    }
    @Override
    public List<BlockPos> getConnectedTo() {
        return connectedTo;
    }

    @Override
    public void setNetwork(ShatteredFlowNetwork network) {
        path = network;
    }

    @Override
    public ShatteredFlowNetwork getNetwork() {
        return path;
    }
}
