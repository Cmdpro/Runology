package com.cmdpro.runology.block.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.IRunicEnergyContainer;
import com.cmdpro.runology.api.RuneItem;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.BlockEntityInit;
import com.cmdpro.runology.init.RecipeInit;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.recipe.IRunicRecipe;
import com.cmdpro.runology.recipe.NonMenuCraftingContainer;
import com.cmdpro.runology.screen.RunicWorkbenchMenu;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class RunicWorkbenchBlockEntity extends BlockEntity implements MenuProvider, GeoBlockEntity, IRunicEnergyContainer {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 10) {
                return false;
            }
            if (slot == 0) {
                return stack.getItem() instanceof RuneItem;
            }
            return super.isItemValid(slot, stack);
        }
    };
    public void drops() {
        SimpleContainer inventory = getInv();

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public RunicWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.RUNICWORKBENCH.get(), pos, state);
        item = ItemStack.EMPTY;
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        getRunicEnergy().clear();
        for (Tag i : (ListTag)tag.get("runicEnergy")) {
            getRunicEnergy().put(((CompoundTag)i).getString("key"), ((CompoundTag)i).getFloat("value"));
        }
        runicEnergyCost.clear();
        for (Tag i : (ListTag)tag.get("runicEnergyCost")) {
            runicEnergyCost.put(((CompoundTag)i).getString("key"), ((CompoundTag)i).getFloat("value"));
        }
        item = ItemStack.of(tag.getCompound("item"));
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ListTag tag2 = new ListTag();
        for (Map.Entry<String, Float> i : getRunicEnergy().entrySet()) {
            CompoundTag tag3 = new CompoundTag();
            tag3.putString("key", i.getKey());
            tag3.putFloat("value", i.getValue());
            tag2.add(tag3);
        }
        tag.put("runicEnergy", tag2);
        ListTag tag4 = new ListTag();
        for (Map.Entry<String, Float> i : runicEnergyCost.entrySet()) {
            CompoundTag tag3 = new CompoundTag();
            tag3.putString("key", i.getKey());
            tag3.putFloat("value", i.getValue());
            tag4.add(tag3);
        }
        tag.put("runicEnergyCost", tag4);
        tag.put("item", item.save(new CompoundTag()));
        return tag;
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        ListTag tag2 = new ListTag();
        for (Map.Entry<String, Float> i : getRunicEnergy().entrySet()) {
            CompoundTag tag3 = new CompoundTag();
            tag3.putString("key", i.getKey());
            tag3.putFloat("value", i.getValue());
            tag2.add(tag3);
        }
        tag.put("runicEnergy", tag2);
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        getRunicEnergy().clear();
        for (Tag i : (ListTag)nbt.get("runicEnergy")) {
            getRunicEnergy().put(((CompoundTag)i).getString("key"), ((CompoundTag)i).getFloat("value"));
        }
    }
    public ItemStack item;
    public SimpleContainer getInv() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    public CraftingContainer getCraftingInv() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            items.add(itemHandler.getStackInSlot(i));
        }
        CraftingContainer inventory = new NonMenuCraftingContainer(items, 3, 3);
        return inventory;
    }
    public static InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                        Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof RunicWorkbenchBlockEntity ent) {
            if (ent.recipe != null) {
                if (ent.enoughRunicEnergy) {
                    if (RunologyUtil.playerHasEntry(pPlayer, ent.recipe.getEntry())) {
                        for (int i = 1; i < 10; i++) {
                            ent.itemHandler.getStackInSlot(i).shrink(1);
                        }
                        ItemStack stack = ent.recipe.assemble(ent.getCraftingInv(), pLevel.registryAccess());
                        ItemEntity entity = new ItemEntity(pLevel, (float) pPos.getX() + 0.5f, (float) pPos.getY() + 1f, (float) pPos.getZ() + 0.5f, stack);
                        pLevel.addFreshEntity(entity);
                        pLevel.playSound(null, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 2, 1);
                        float waste = 0;
                        for (Map.Entry<String, Float> i : ent.recipe.getRunicEnergyCost().entrySet()) {
                            ent.getRunicEnergy().put(i.getKey(), ent.getRunicEnergy().get(i.getKey())-i.getValue());
                            waste += i.getValue();
                        }
                        waste /= 5;
                        RunologyUtil.displayInstabilityGen(pLevel, pPos.getCenter(), new Vec3(0, 1, 0));
                        RunologyUtil.AddInstability(pLevel.getChunkAt(pPos).getPos(), pLevel, waste, 0, ChunkModData.MAX_INSTABILITY);
                    } else {
                        pPlayer.sendSystemMessage(Component.translatable("block.runology.runicworkbench.dontknowhow"));
                    }
                } else {
                    pPlayer.sendSystemMessage(Component.translatable("block.runology.runicworkbench.notenoughenergy"));
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    public float getTotalRunicEnergy() {
        float energy = 0;
        for (Map.Entry<String, Float> i : runicEnergy.entrySet()) {
            energy += i.getValue();
        }
        return energy;
    }
    public IRunicRecipe recipe;
    public boolean enoughRunicEnergy;
    public Map<String, Float> runicEnergyCost = new HashMap<>();
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, RunicWorkbenchBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pBlockEntity.itemHandler.getStackInSlot(0).getItem() instanceof RuneItem rune && !pBlockEntity.itemHandler.getStackInSlot(0).isEmpty()) {
                if (pBlockEntity.getRunicEnergy().getOrDefault(rune.runicEnergyType.toString(), 0f)+50 <= 1000) {
                    pBlockEntity.addRunicEnergy(rune.runicEnergyType.toString(), 50);
                    pBlockEntity.itemHandler.getStackInSlot(0).shrink(1);
                }
            }
            Optional<IRunicRecipe> recipe = pLevel.getRecipeManager().getRecipeFor(RecipeInit.RUNICCRAFTING.get(), pBlockEntity.getCraftingInv(), pLevel);
            if (recipe.isPresent()) {
                pBlockEntity.recipe = recipe.get();
                pBlockEntity.runicEnergyCost = recipe.get().getRunicEnergyCost();
                pBlockEntity.item = recipe.get().getResultItem(pLevel.registryAccess());
                boolean enoughEnergy = true;
                for (Map.Entry<String, Float> i : recipe.get().getRunicEnergyCost().entrySet()) {
                    if (pBlockEntity.getRunicEnergy().containsKey(i.getKey())) {
                        if (pBlockEntity.getRunicEnergy().get(i.getKey()) < i.getValue()) {
                            enoughEnergy = false;
                            break;
                        }
                    } else {
                        enoughEnergy = false;
                        break;
                    }
                }
                pBlockEntity.enoughRunicEnergy = enoughEnergy;
            } else {
                pBlockEntity.recipe = null;
                if (!pBlockEntity.runicEnergyCost.isEmpty()) {
                    pBlockEntity.runicEnergyCost = new HashMap<>();
                }
                pBlockEntity.item = ItemStack.EMPTY;
            }
            pBlockEntity.updateBlock();
        }
    }
    protected void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        if (!item.isEmpty()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.runicworkbench.ready", Animation.LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("animation.runicworkbench.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.runology.runicworkbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new RunicWorkbenchMenu(pContainerId, pInventory, this);
    }
    private static boolean hasNotReachedStackLimit(RunicWorkbenchBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(2).getCount() < entity.itemHandler.getStackInSlot(2).getMaxStackSize();
    }
    Map<String, Float> runicEnergy = new HashMap<>();
    @Override
    public Map<String, Float> getRunicEnergy() {
        return runicEnergy;
    }
}
