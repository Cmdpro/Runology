package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.shatteredflow.ContainsShatteredFlow;
import com.cmdpro.runology.api.shatteredflow.NormalShatteredFlowStorage;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.api.shatteredflow.SmartShatteredFlowStorage;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.chunkloading.ChunkloadingEventHandler;
import com.cmdpro.runology.registry.*;
import com.ibm.icu.impl.coll.CollationKeys;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.ForceLoadCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.world.chunk.ForcedChunkManager;
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
        realityReshaperProgress = -1;
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
    public int realityReshaperProgress;
    public List<BlockPos> connectedTo;
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        ResourceLocation realityReshaperId = ResourceLocation.fromNamespaceAndPath(Runology.MODID, "reality_reshaper");
        LevelChunk chunk = pLevel.getChunkAt(pPos);
        if (pLevel.getBlockEntity(pPos.above()) instanceof ShatterBlockEntity shatter) {
            if (pLevel.isClientSide) {
                if (isPowered) {
                    Vec3 center = shatter.getBlockPos().getCenter();
                    Vec3 diff = pPos.getBottomCenter().add(0, 0.1, 0).subtract(center).multiply(0.2f, 0.2f, 0.2f);
                    pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
                }
            } else {
                ServerLevel level = (ServerLevel)pLevel;
                if (!level.getForcedChunks().contains(chunk.getPos().toLong())) {
                    ChunkloadingEventHandler.shatterController.forceChunk(level, getBlockPos(), chunk.getPos().x, chunk.getPos().z, true, true);
                }
                storage.addShatteredFlow(shatter.getOutputShatteredFlow());
                Multiblock realityReshaperMultiblock = MultiblockDataManager.get().getMultiblock(realityReshaperId);
                if (realityReshaperMultiblock.validate(pLevel, pPos.below(), Rotation.NONE)) {
                    if (realityReshaperProgress < 0) {
                        realityReshaperProgress = 0;
                    }
                }
            }
        } else {
            if (!pLevel.isClientSide) {
                ServerLevel level = (ServerLevel) pLevel;
                ChunkloadingEventHandler.shatterController.forceChunk(level, getBlockPos(), chunk.getPos().x, chunk.getPos().z, false, true);
            }
        }
        if (pLevel.isClientSide) {
            pLevel.addParticle(ParticleRegistry.SHATTER.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1);
            if (realityReshaperProgress >= 0) {
                BlockPos[] runes = new BlockPos[] {
                        getBlockPos().above().offset(3, 0, 0),
                        getBlockPos().above().offset(-3, 0, 0),
                        getBlockPos().above().offset(0, 0, 3),
                        getBlockPos().above().offset(0, 0, -3),
                        getBlockPos().above().offset(2, 0, 2),
                        getBlockPos().above().offset(-2, 0, 2),
                        getBlockPos().above().offset(-2, 0, -2),
                        getBlockPos().above().offset(2, 0, -2)
                };
                int[] bursts = new int[] { 0, 20, 40, 60, 75, 85, 95, 105, 115, 125 };
                for (int i : bursts) {
                    if (realityReshaperProgress >= i && realityReshaperProgress <= i+5) {
                        for (BlockPos j : runes) {
                            Vec3 center = j.getCenter();
                            Vec3 diff = pPos.above().getCenter().subtract(center).multiply(0.2f, 0.2f, 0.2f);
                            pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
                        }
                    }
                }
                if (realityReshaperProgress >= 90 && realityReshaperProgress <= 140) {
                    for (BlockPos j : runes) {
                        Vec3 center = pPos.above().getCenter().add(j.getCenter().subtract(pPos.above().getCenter()).yRot((float)Math.toRadians((realityReshaperProgress-90)*5)));
                        Vec3 diff = pPos.above().getCenter().subtract(center).multiply(0.2f, 0.2f, 0.2f);
                        pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
                    }
                }
            }
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
            if (realityReshaperProgress >= 0) {
                realityReshaperProgress++;
                Multiblock realityReshaperMultiblock = MultiblockDataManager.get().getMultiblock(realityReshaperId);
                if (!realityReshaperMultiblock.validate(pLevel, pPos.below(), Rotation.NONE)) {
                    realityReshaperProgress = -1;
                }
                if (realityReshaperProgress >= 170) {
                    level.setBlockAndUpdate(pPos.above(), Blocks.AIR.defaultBlockState());
                    level.explode(null, pPos.above().getCenter().x, pPos.above().getCenter().y, pPos.above().getCenter().z, 8, true, Level.ExplosionInteraction.TNT);
                    for (int i = 0; i < 8; i++) {
                        Vec3 dir = Vec3.directionFromRotation(level.getRandom().nextFloat()*-90, level.getRandom().nextFloat()*360);
                        ItemEntity entity = new ItemEntity(level, pPos.above().getCenter().x+dir.x, pPos.above().getCenter().y+dir.y, pPos.above().getCenter().z+dir.z, new ItemStack(ItemRegistry.SHATTERED_SHARD.get()));
                        entity.setDeltaMovement(dir);
                        level.addFreshEntity(entity);
                    }
                    realityReshaperProgress = 0;
                }
                updateBlock();
            }
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
        ShatteredFlowNetwork.updatePaths(level, getBlockPos(), new ShatteredFlowNetwork(new ArrayList<>(), new ArrayList<>()), new ArrayList<>());
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
        tag.putInt("realityReshaperProgress", realityReshaperProgress);
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
        realityReshaperProgress = tag.getInt("realityReshaperProgress");
    }
}
