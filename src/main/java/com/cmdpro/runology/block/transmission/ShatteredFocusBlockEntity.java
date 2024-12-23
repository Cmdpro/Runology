package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.shatteredflow.ContainsShatteredFlow;
import com.cmdpro.runology.api.shatteredflow.NormalShatteredFlowStorage;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.api.shatteredflow.SmartShatteredFlowStorage;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.*;
import com.ibm.icu.impl.coll.CollationKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.commands.ForceLoadCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShatteredFocusBlockEntity extends BlockEntity implements ContainsShatteredFlow {
    public SmartShatteredFlowStorage storage = new SmartShatteredFlowStorage();
    @Override
    public SmartShatteredFlowStorage getShatteredFlowStorage() {
        return storage;
    }
    public ShatteredFocusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTERED_FOCUS.get(), pos, state);
        connectedTo = new ArrayList<>();
    }
    public ShatteredFlowNetwork path;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.put("storage", storage.toTag());
        ListTag list = new ListTag();
        for (BlockPos i : connectedTo) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
    }
    public boolean isPowered;
    @Override
    public void setLevel(Level pLevel) {
        if (level != null) {
            level.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES).remove(this);
        }
        super.setLevel(pLevel);
        if (!pLevel.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES).contains(this)) {
            pLevel.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES).add(this);
        }
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        storage.fromTag(tag.getCompound("storage"));
        connectedTo.clear();
        if (tag.contains("link")) {
            ListTag list = (ListTag) tag.get("link");
            for (Tag i : list) {
                CompoundTag blockpos = (CompoundTag) i;
                connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
            }
        }
    }
    public List<BlockPos> connectedTo;
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        LevelChunk chunk = pLevel.getChunkAt(pPos);
        if (pLevel.getBlockEntity(pPos.above()) instanceof ShatterBlockEntity shatter) {
            if (pLevel.isClientSide) {
                Vec3 center = shatter.getBlockPos().getCenter();
                Vec3 diff = pPos.getBottomCenter().add(0, 0.1, 0).subtract(center).multiply(0.2f, 0.2f, 0.2f);
                pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
            } else {
                ServerLevel level = (ServerLevel)pLevel;
                if (!level.getForcedChunks().contains(chunk.getPos().toLong())) {
                    ((ServerLevel)pLevel).setChunkForced(chunk.getPos().x, chunk.getPos().z, true);
                }
                storage.addShatteredFlow(shatter.getOutputShatteredFlow());
            }
        } else {
            if (!pLevel.isClientSide) {
                ServerLevel level = (ServerLevel)pLevel;
                if (level.getForcedChunks().contains(chunk.getPos().toLong())) {
                    ((ServerLevel)pLevel).setChunkForced(chunk.getPos().x, chunk.getPos().z, false);
                }
            }
        }
        if (pLevel.isClientSide) {
            pLevel.addParticle(ParticleRegistry.SHATTER.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1);
        } else {
            boolean powered = false;
            for (BlockPos i : path.starts) {
                if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent) {
                    if (ent.storage.amount > 0) {
                        powered = true;
                        break;
                    }
                }
            }
            if (isPowered != powered) {
                updateBlock();
            }
            isPowered = powered;
        }
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        for (ShatteredRelayBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS)) {
            if (i.getBlockPos().getCenter().distanceTo(getBlockPos().getCenter()) <= 20) {
                if (!i.connectedTo.contains(getBlockPos())) {
                    i.connectedTo.add(getBlockPos());
                    i.updateBlock();
                }
                if (!connectedTo.contains(i.getBlockPos())) {
                    connectedTo.add(i.getBlockPos());
                }
            }
        }
        ShatteredFlowNetwork.updatePaths(level, getBlockPos(), new ShatteredFlowNetwork(new ArrayList<>()), new ArrayList<>());
        updateBlock();
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
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ListTag list = (ListTag)tag.get("link");
        connectedTo.clear();
        for (Tag i : list) {
            CompoundTag blockpos = (CompoundTag)i;
            connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
        }
        isPowered = tag.getBoolean("isPowered");
    }
}
