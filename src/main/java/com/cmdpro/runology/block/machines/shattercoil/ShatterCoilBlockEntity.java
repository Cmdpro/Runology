package com.cmdpro.runology.block.machines.shattercoil;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowConnectable;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.entity.ShatterZap;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.DamageTypeRegistry;
import com.cmdpro.runology.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class ShatterCoilBlockEntity extends BlockEntity implements ShatteredFlowConnectable {
    public ShatterCoilBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTER_COIL.get(), pos, state);
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (!pLevel.isClientSide) {
            if (tryUseEnergy(level, 10) <= 0) {
                for (LivingEntity i : level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pPos.getCenter(), 30, 30, 30))) {
                    if (!(i instanceof Player)) {
                        if (i.position().distanceTo(pPos.getCenter()) <= 15) {
                            if (i.hurt(i.damageSources().source(DamageTypeRegistry.shatterZap), 5)) {
                                ShatterZap attack = new ShatterZap(EntityRegistry.SHATTER_ZAP.get(), pPos.getCenter().add(0, 0.5f, 0), pLevel, i);
                                pLevel.addFreshEntity(attack);
                            }
                        }
                    }
                }
            }
        }
    }
    public ShatteredFlowNetwork path;
    public List<BlockPos> connectedTo = new ArrayList<>();
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

    @Override
    public void onLoad() {
        super.onLoad();
        onLoad(level, getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        addToTag(level, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        getFromTag(getBlockPos(), tag, registries);
    }
}
