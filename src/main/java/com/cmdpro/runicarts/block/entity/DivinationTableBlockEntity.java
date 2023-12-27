package com.cmdpro.runicarts.block.entity;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.ISoulContainer;
import com.cmdpro.runicarts.api.SoulGem;
import com.cmdpro.runicarts.init.BlockEntityInit;
import com.cmdpro.runicarts.init.ItemInit;
import com.cmdpro.runicarts.init.ParticleInit;
import com.cmdpro.runicarts.recipe.SoulAltarRecipe;
import com.cmdpro.runicarts.screen.DivinationTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
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

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DivinationTableBlockEntity extends BlockEntity implements MenuProvider, ISoulContainer, GeoBlockEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private float souls;

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (slot == 1) {
                if (stack.getItem() instanceof SoulGem) {
                    return true;
                }
                return false;
            }
            if (slot == 2) {
                if (stack.isEmpty()) {
                    return true;
                }
                return false;
            }
            return true;
        }
    };
    public void drops() {
        SimpleContainer inventory = getInv();

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    public DivinationTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DIVINATIONTABLE.get(), pos, state);
        item = ItemStack.EMPTY;
        linked = new ArrayList<>();
        this.data = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0: return DivinationTableBlockEntity.this.progress;
                    case 1: return DivinationTableBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: DivinationTableBlockEntity.this.progress = value; break;
                    case 1: DivinationTableBlockEntity.this.maxProgress = value; break;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        return super.getCapability(cap, side);
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
    public float getMaxSouls() {
        return 20;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private List<BlockPos> linked;
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        item = ItemStack.of(tag.getCompound("item"));
        souls = tag.getFloat("souls");
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("item", item.save(new CompoundTag()));
        tag.putFloat("souls", souls);
        return tag;
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putFloat("souls", souls);
        tag.put("inventory", itemHandler.serializeNBT());
        if (!this.linked.isEmpty()) {
            ListTag listtag = new ListTag();
            for(BlockPos i : this.linked) {
                CompoundTag tag2 = new CompoundTag();
                tag2.putInt("x", i.getX());
                tag2.putInt("y", i.getY());
                tag2.putInt("z", i.getZ());
                listtag.add(tag2);
            }
            tag.put("linked", listtag);
        }
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        souls = nbt.getFloat("souls");
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        linked.clear();
        if (nbt.contains("linked")) {
            ((ListTag) nbt.get("linked")).forEach((i) -> {
                CompoundTag i2 = ((CompoundTag)i);
                BlockPos pos = new BlockPos(i2.getInt("x"), i2.getInt("y"), i2.getInt("z"));
                linked.add(pos);
            });
        }
    }
    @Override
    public float getSouls() {
        return souls;
    }
    @Override
    public void setSouls(float amount) {
        souls = amount;
    }

    @Override
    public List<BlockPos> getLinked() {
        return linked;
    }
    public ItemStack item;
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
    }
    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DivinationTableBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            int progress = 0;
            ItemStack stack = pBlockEntity.itemHandler.getStackInSlot(1);
            List<String> items = new ArrayList<>();
            if (stack.hasTag()) {
                if (stack.getTag().contains("items")) {
                    ListTag tag = (ListTag)stack.getTag().get("items");
                    progress = tag.size();
                    for (Tag i : tag) {
                        items.add(((CompoundTag)i).getString("item"));
                    }
                }
            }
            pBlockEntity.item = pBlockEntity.itemHandler.getStackInSlot(1);
            pBlockEntity.progress = progress;
            int maxProgress = 1;
            if (stack.getItem() instanceof SoulGem gem) {
                maxProgress = gem.itemCost;
            }
            pBlockEntity.maxProgress = maxProgress;
            ItemStack stack2 = pBlockEntity.itemHandler.getStackInSlot(0);
            if (!items.contains(stack2.getDescriptionId()) && !stack2.isEmpty() && pBlockEntity.souls >= 2 && !stack.isEmpty()) {
                items.add(stack2.getDescriptionId());
                pBlockEntity.itemHandler.extractItem(0, 1, false);
                pBlockEntity.souls -= 2;
            }
            if (progress >= maxProgress) {
                if (stack.getItem() instanceof SoulGem gem && hasNotReachedStackLimit(pBlockEntity)) {
                    ItemStack stack3 = new ItemStack(ItemInit.STUDYRESULTS.get(), pBlockEntity.itemHandler.getStackInSlot(2).getCount() + 1);
                    stack3.getOrCreateTag().putInt("studytype", gem.rewardType);
                    stack3.getOrCreateTag().putInt("studyamount", gem.rewardAmount);
                    pBlockEntity.itemHandler.setStackInSlot(2, stack3);
                    pBlockEntity.itemHandler.extractItem(1, 1, false);
                }
            }
            if (items.size() > 0) {
                ListTag list = new ListTag();
                for (String i : items) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("item", i);
                    list.add(tag);
                }
                stack.getOrCreateTag().put("items", list);
            }
            pBlockEntity.updateBlock();
            pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
        }
        pBlockEntity.soulContainerTick(pLevel, pPos, pState, pBlockEntity);
    }
    protected void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        if (!item.isEmpty()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.divinationtable.studying", Animation.LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("animation.divinationtable.idle", Animation.LoopType.LOOP));
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
        return Component.translatable("block.runicarts.divinationtable");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new DivinationTableMenu(pContainerId, pInventory, this, this.data);
    }
    private static boolean hasNotReachedStackLimit(DivinationTableBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(2).getCount() < entity.itemHandler.getStackInSlot(2).getMaxStackSize();
    }
}
