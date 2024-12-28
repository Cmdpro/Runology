package com.cmdpro.runology.block.machines;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowConnectable;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.recipe.RecipeUtil;
import com.cmdpro.runology.recipe.ShatterImbuementRecipe;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.registry.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShatteredInfuserBlockEntity extends BlockEntity implements ShatteredFlowConnectable {
    public final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            updateBlock();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return super.isItemValid(slot, stack);
        }
    };
    public IItemHandler getItemHandler() {
        return lazyItemHandler.get();
    }
    private Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> itemHandler);
    public SimpleContainer getInv() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    public ShatteredInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTERED_INFUSER.get(), pos, state);
        item = ItemStack.EMPTY;
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, Block.UPDATE_CLIENTS);
        this.setChanged();
    }
    public ItemStack item;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inventory", itemHandler.serializeNBT(provider));
        super.saveAdditional(tag, provider);
    }
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        itemHandler.deserializeNBT(provider, nbt.getCompound("inventory"));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (!pkt.getTag().isEmpty()) {
            decodeUpdateTag(pkt.getTag(), lookupProvider);
        }
    }
    public int craftingTime = -1;
    public ResourceLocation currentRecipe;
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        RecipeInput input;
        if (pLevel.isClientSide) {
            input = new SingleRecipeInput(item);
        } else {
            input = new SingleRecipeInput(itemHandler.getStackInSlot(0));
        }
        if (pLevel.isClientSide) {
            if (crafting) {
                List<Vec3> starts = new ArrayList<>();
                starts.add(pPos.getCenter().add(0.5, 0.25, 0));
                starts.add(pPos.getCenter().add(-0.5, 0.25, 0));
                starts.add(pPos.getCenter().add(0, 0.25, 0.5));
                starts.add(pPos.getCenter().add(0, 0.25, -0.5));
                for (Vec3 i : starts) {
                    Vec3 diff = pPos.getCenter().add(0, 0.25, 0).subtract(i).multiply(0.2f, 0.2f, 0.2f);
                    pLevel.addParticle(ParticleRegistry.SMALL_SHATTER.get(), i.x, i.y, i.z, diff.x, diff.y, diff.z);
                }
            }
        } else {
            Optional<RecipeHolder<ShatterImbuementRecipe>> recipe = RecipeUtil.getShatterImbuementRecipe(level, input);
            if (recipe.isPresent()) {
                if (!crafting) {
                    crafting = true;
                    updateBlock();
                }
                if (!recipe.get().id().equals(currentRecipe)) {
                    craftingTime = 0;
                }
                tryUseEnergy(50);
                currentRecipe = recipe.get().id();
                craftingTime++;
                if (craftingTime >= 50) {
                    ItemStack book = recipe.get().value().getResultItem(level.registryAccess());
                    ItemEntity item = new ItemEntity(pLevel, getBlockPos().getCenter().x, getBlockPos().getCenter().y, getBlockPos().getCenter().z, book);
                    pLevel.addFreshEntity(item);
                    ItemStack newStack = itemHandler.getStackInSlot(0).copy();
                    newStack.shrink(1);
                    itemHandler.setStackInSlot(0, newStack);
                    craftingTime = 0;
                }
            } else {
                if (crafting) {
                    crafting = false;
                    updateBlock();
                }
                craftingTime = -1;
                currentRecipe = null;
            }
        }
    }
    public boolean crafting;
    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        decodeUpdateTag(tag, lookupProvider);
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        item = ItemStack.parseOptional(lookupProvider, tag.getCompound("item"));
        crafting = tag.getBoolean("crafting");
    }


    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, getInv());
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("item", itemHandler.getStackInSlot(0).saveOptional(provider));
        tag.putBoolean("crafting", crafting);
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
}
