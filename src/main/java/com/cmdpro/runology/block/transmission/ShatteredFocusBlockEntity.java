package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.shatteredflow.ContainsShatteredFlow;
import com.cmdpro.runology.api.shatteredflow.NormalShatteredFlowStorage;
import com.cmdpro.runology.api.shatteredflow.SmartShatteredFlowStorage;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ShatteredFocusBlockEntity extends BlockEntity implements ContainsShatteredFlow {
    public SmartShatteredFlowStorage storage = new SmartShatteredFlowStorage();
    @Override
    public SmartShatteredFlowStorage getShatteredFlowStorage() {
        return storage;
    }
    public ShatteredFocusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTERED_FOCUS.get(), pos, state);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.put("storage", storage.toTag());
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        storage.fromTag(tag.getCompound("storage"));
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.getBlockEntity(pPos.above()) instanceof ShatterBlockEntity shatter) {
            if (pLevel.isClientSide) {
                Vec3 center = shatter.getBlockPos().getCenter();
                Vec3 diff = pPos.getBottomCenter().add(0, 0.1, 0).subtract(center).multiply(0.2f, 0.2f, 0.2f);
                pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
            } else {
                storage.addShatteredFlow(shatter.getOutputShatteredFlow());
            }
        }
    }
}
