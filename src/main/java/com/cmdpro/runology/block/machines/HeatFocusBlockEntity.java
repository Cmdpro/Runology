package com.cmdpro.runology.block.machines;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowConnectable;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.recipe.RecipeUtil;
import com.cmdpro.runology.recipe.ShatterImbuementRecipe;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.registry.RecipeRegistry;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
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
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

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
            Multiblock heatFocusMultiblock = MultiblockDataManager.get().getMultiblock(heatFocusId);
            if (heatFocusMultiblock.validate(pLevel, pPos, Rotation.NONE) && path != null && hasEnergy(level, 50) <= 0) {
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
                        i.setData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER, i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER)+1);
                        if (i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER) % 10 == 0) {
                            i.playSound(SoundEvents.FURNACE_FIRE_CRACKLE);
                            ((ServerLevel)i.level()).sendParticles(ParticleTypes.FLAME, i.position().x, i.position().y, i.position().z, 15, 0, 0, 0, 0.2f);
                        }
                        if (i.getData(AttachmentTypeRegistry.HEAT_FOCUS_SMELT_TIMER) >= recipe.get().value().getCookingTime()) {
                            ItemStack stack = recipe.get().value().assemble(input, level.registryAccess());
                            stack.setCount(i.getItem().getCount());
                            i.setItem(stack);
                            i.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE);
                        }
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
        addToTag(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        getFromTag(level, tag);
    }
}
