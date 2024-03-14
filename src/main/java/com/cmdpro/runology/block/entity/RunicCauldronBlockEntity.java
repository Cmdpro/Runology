package com.cmdpro.runology.block.entity;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.BlockEntityInit;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.item.Research;
import com.cmdpro.runology.moddata.PlayerModData;
import com.cmdpro.runology.moddata.PlayerModDataProvider;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import com.cmdpro.runology.screen.RunicAnalyzerMenu;
import com.klikli_dev.modonomicon.fluid.ForgeFluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class RunicCauldronBlockEntity extends BlockEntity implements GeoBlockEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.getItem() instanceof Research;
            }
            return super.isItemValid(slot, stack);
        }
    };
    private final FluidTank fluidHandler = new FluidTank(1000);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public RunicCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.RUNICCAULDRON.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        if (side == null) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return lazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        fluidHandler.readFromNBT(tag.getCompound("fluid"));
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("fluid", fluidHandler.writeToNBT(new CompoundTag()));
        return tag;
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.put("fluid", fluidHandler.writeToNBT(new CompoundTag()));
        if (owner != null) {
            tag.putUUID("owner", owner);
        }
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        fluidHandler.readFromNBT(nbt.getCompound("fluid"));
        if (nbt.contains("owner")) {
            owner = nbt.getUUID("owner");
        }
    }
    public SimpleContainer getInv() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }
    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }
    public FluidStack getFluid() {
        return fluidHandler.getFluid();
    }
    public boolean isFluidEmpty() {
        return fluidHandler.isEmpty();
    }
    public int getFluidAmount() {
        return fluidHandler.getFluidAmount();
    }
    public int getCapacity() {
        return fluidHandler.getCapacity();
    }
    private UUID owner;
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    public UUID getOwner() {
        return owner;
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, RunicCauldronBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            ItemStack stack = pBlockEntity.itemHandler.getStackInSlot(0);
            FluidStack fluid = pBlockEntity.fluidHandler.getFluidInTank(0);
            if (stack.isEmpty() && !fluid.isEmpty()) {
                for (ItemEntity i : pLevel.getEntitiesOfClass(ItemEntity.class, pBlockEntity.getRenderBoundingBox().deflate(0.25))) {
                    ItemStack stack2 = pBlockEntity.itemHandler.getStackInSlot(0);
                    if (stack2.isEmpty()) {
                        pBlockEntity.itemHandler.setStackInSlot(0, i.getItem().copy());
                        i.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
            while (!stack.isEmpty() && !fluid.isEmpty()) {
                RunicCauldronItemRecipe recipe = pBlockEntity.getRecipe();
                Player player = pLevel.getServer().getPlayerList().getPlayer(pBlockEntity.owner);
                if (recipe == null || !RunologyUtil.playerHasEntry(player, recipe.getEntry())) {
                    fluid.shrink(1000);
                    stack.shrink(1);
                    pBlockEntity.updateBlock();
                    pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);
                    ItemEntity item = new ItemEntity(pLevel, pPos.getCenter().x, pPos.getCenter().y+0.25, pPos.getCenter().z, new ItemStack(ItemInit.RUNICWASTE.get()));
                    item.setNoGravity(true);
                    item.setDeltaMovement(0, 0, 0);
                    pLevel.addFreshEntity(item);
                    stack.shrink(1);
                    break;
                } else {
                    fluid.shrink(recipe.getFluidInput().getAmount());
                    ItemEntity item = new ItemEntity(pLevel, pPos.getCenter().x, pPos.getCenter().y+0.25, pPos.getCenter().z, recipe.getResultItem(pLevel.registryAccess()));
                    item.setNoGravity(true);
                    item.setDeltaMovement(0, 0, 0);
                    pLevel.addFreshEntity(item);
                    stack.shrink(1);
                    pBlockEntity.updateBlock();
                    pLevel.playSound(null, pPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS);
                    pLevel.playSound(null, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
                }
            }
            if (fluid.isEmpty() && !stack.isEmpty()) {
                ItemEntity item = new ItemEntity(pLevel, pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, stack);
                pLevel.addFreshEntity(item);
                pBlockEntity.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            }
        }
    }
    public RunicCauldronItemRecipe getRecipe() {
        SimpleContainer inv = getInv();
        List<RunicCauldronItemRecipe> recipes = level.getRecipeManager().getRecipesFor(RunicCauldronItemRecipe.Type.INSTANCE, inv, level);
        for (RunicCauldronItemRecipe i : recipes) {
            if (i.getFluidInput().isFluidEqual(fluidHandler.getFluid()) && fluidHandler.getFluidAmount() >= i.getFluidInput().getAmount()) {
                return i;
            }
        }
        return null;
    }
    protected void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.runiccauldron.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown()) {
            if (!fluidHandler.isEmpty()) {
                fluidHandler.getFluid().shrink(1000);
                itemHandler.getStackInSlot(0).setCount(0);
                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);
                updateBlock();
            }
        } else {
            if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, fluidHandler)) {
                updateBlock();
            } else {
                Player player = pLevel.getServer().getPlayerList().getPlayer(owner);
                pPlayer.sendSystemMessage(Component.translatable("block.runology.runiccauldron.owner", player.getDisplayName()));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
