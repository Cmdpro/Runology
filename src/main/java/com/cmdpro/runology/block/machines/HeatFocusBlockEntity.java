package com.cmdpro.runology.block.machines;

import com.cmdpro.databank.multiblock.Multiblock;
import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowConnectable;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeatFocusBlockEntity extends BlockEntity implements ShatteredFlowConnectable {
    public HeatFocusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.HEAT_FOCUS.get(), pos, state);
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, Block.UPDATE_CLIENTS);
        this.setChanged();
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (!pkt.getTag().isEmpty()) {
            decodeUpdateTag(pkt.getTag(), lookupProvider);
        }
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        ResourceLocation heatFocusId = Runology.locate("heat_focus");
        if (pLevel.isClientSide) {
            if (hasEnergy) {
                for (int i = 0; i < 10; i++) {
                    List<Vec3> starts = new ArrayList<>();
                    starts.add(pPos.getBottomCenter().add((level.random.nextFloat()-0.5f)*0.5f, 0, (level.random.nextFloat()-0.5f)*0.5f));
                    for (Vec3 j : starts) {
                        pLevel.addParticle(ParticleTypes.FLAME, j.x, j.y, j.z, 0, 0.6f*level.random.nextFloat(), 0);
                    }
                }
            }
        } else {
            Multiblock heatFocusMultiblock = MultiblockManager.multiblocks.get(heatFocusId);
            if (heatFocusMultiblock.checkMultiblock(pLevel, pPos, Rotation.NONE) && path != null && hasEnergy(level, 50) <= 0) {
                if (!hasEnergy) {
                    hasEnergy = true;
                    updateBlock();
                }
            } else {
                if (hasEnergy) {
                    hasEnergy = false;
                    updateBlock();
                }
            }
            tryUseEnergy(level, 50);
            if (hasEnergy) {
                for (ItemEntity i : level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(pPos.getBottomCenter().add(0, 1.5f, 0), 0.75f, 3f, 0.75f))) {
                    SingleRecipeInput input = new SingleRecipeInput(i.getItem());
                    Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, level);
                    if (recipe.isPresent()) {
                        int cookTime = recipe.get().value().getCookingTime()/4;
                        i.setData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER, i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER)+1);
                        if (i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER) % 10 == 0) {
                            i.playSound(SoundRegistry.HEAT_FOCUS_WORKING.value());
                            ((ServerLevel)i.level()).sendParticles(ParticleTypes.FLAME, i.position().x, i.position().y, i.position().z, 15, 0, 0, 0, 0.2f);
                        }
                        int smelting = Math.min(i.getItem().getCount(), 16);
                        if (i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER) >= cookTime) {
                            ItemStack stack = recipe.get().value().assemble(input, level.registryAccess());
                            int count = stack.getCount()*smelting;
                            Vec3 pos = i.position();
                            while (count > 64) {
                                ItemStack stack2 = stack.copy();
                                stack2.setCount(64);
                                ItemEntity entity = new ItemEntity(pLevel, pos.x, pos.y, pos.z, stack2);
                                pLevel.addFreshEntity(entity);
                                count -= 64;
                            }
                            stack.setCount(count);
                            ItemEntity entity = new ItemEntity(pLevel, pos.x, pos.y, pos.z, stack);
                            pLevel.addFreshEntity(entity);
                            ItemStack entityStack = i.getItem().copy();
                            entityStack.shrink(smelting);
                            i.setItem(entityStack);
                            i.removeData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER);
                            i.playSound(SoundRegistry.HEAT_FOCUS_FINISH.value());
                        }
                        break;
                    } else {
                        if (i.hasData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER)) {
                            i.removeData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER);
                        }
                    }
                }
            }
        }
    }
    public boolean hasEnergy;
    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        decodeUpdateTag(tag, lookupProvider);
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        hasEnergy = tag.getBoolean("hasEnergy");
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hasEnergy", hasEnergy);
        return tag;
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
    public Vec3 getConnectOffset() {
        return new Vec3(0, -0.5, 0);
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
