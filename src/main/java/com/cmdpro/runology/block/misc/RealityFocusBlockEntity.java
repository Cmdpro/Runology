package com.cmdpro.runology.block.misc;

import com.cmdpro.databank.multiblock.Multiblock;
import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.api.shatteredflow.ContainsShatteredFlow;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.api.shatteredflow.SmartShatteredFlowStorage;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.chunkloading.ChunkloadingEventHandler;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RealityFocusBlockEntity extends BlockEntity {
    public RealityFocusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.REALITY_FOCUS.get(), pos, state);
        realityReshaperProgress = -1;
    }
    public ShatteredFlowNetwork path;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
    }
    public int realityReshaperProgress;
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        ResourceLocation realityReshaperId = Runology.locate("reality_reshaper");
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
            Multiblock realityReshaperMultiblock = MultiblockManager.multiblocks.get(realityReshaperId);
            if (realityReshaperMultiblock.checkMultiblock(pLevel, pPos.below())) {
                if (realityReshaperProgress < 0) {
                    realityReshaperProgress = 0;
                }
            }
            if (realityReshaperProgress >= 0) {
                realityReshaperProgress++;
                if (!realityReshaperMultiblock.checkMultiblock(pLevel, pPos.below())) {
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
        tag.putInt("realityReshaperProgress", realityReshaperProgress);
        return tag;
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        realityReshaperProgress = tag.getInt("realityReshaperProgress");
    }
}
