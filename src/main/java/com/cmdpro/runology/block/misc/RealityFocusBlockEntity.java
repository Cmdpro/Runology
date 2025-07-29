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
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos.above()) instanceof ShatterBlockEntity) {
                Vec3 centerDiff = pPos.above().getCenter().subtract(pPos.getBottomCenter()).multiply(0.2f, 0.2f, 0.2f);
                pLevel.addParticle(ParticleRegistry.SHATTER.get(), pPos.getCenter().x, pPos.getBottomCenter().y, pPos.getCenter().z, centerDiff.x, centerDiff.y, centerDiff.z);
            }
        }
    }
}
