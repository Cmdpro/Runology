package com.cmdpro.runicarts.block.entity;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.ISoulContainer;
import com.cmdpro.runicarts.init.BlockEntityInit;
import com.cmdpro.runicarts.init.ParticleInit;
import com.cmdpro.runicarts.recipe.SoulAltarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.crypto.interfaces.PBEKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoulAltarBlockEntity extends BlockEntity implements ISoulContainer, GeoBlockEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private float souls;
    public int craftProgress;

    private final ItemStackHandler focusItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    @Override
    public void onLoad() {
        super.onLoad();
        lazyFocusHandler = LazyOptional.of(() -> focusItemHandler);
    }
    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyFocusHandler.invalidate();
    }
    private LazyOptional<IItemHandler> lazyFocusHandler = LazyOptional.empty();
    public SoulAltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SOULALTAR.get(), pos, state);
        item = ItemStack.EMPTY;
        linked = new ArrayList<>();
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
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("item", item.save(new CompoundTag()));
        return tag;
    }
    public static InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof SoulAltarBlockEntity) {
                SoulAltarBlockEntity altar = (SoulAltarBlockEntity) entity;
                if (altar.explosionTimer <= 0) {
                    if (pPlayer.isShiftKeyDown()) {
                        altar.craftProgress = 0;
                        altar.focusItemHandler.extractItem(0, 1, false);
                        altar.item = ItemStack.EMPTY;
                    } else {
                        if (altar.craftProgress == 0) {
                            ItemStack stack = pPlayer.getItemInHand(pHand).copy();
                            if (stack.getCount() > 0) {
                                stack.setCount(1);
                                pPlayer.getItemInHand(pHand).shrink(1);
                                altar.focusItemHandler.setStackInSlot(0, stack);
                                altar.craftProgress++;
                            }
                        } else {
                            if (altar.item.is(pPlayer.getItemInHand(pHand).getItem())) {
                                pPlayer.getItemInHand(pHand).shrink(1);
                                altar.craftProgress++;
                                pLevel.playSound(null, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 2, 1);
                                ((ServerLevel) pLevel).sendParticles(ParticleInit.SOUL.get(), (float) pPos.getX() + 0.5f, (float) pPos.getY() + 1.5f, (float) pPos.getZ() + 0.5f, 25, 0, 0, 0, 0.1);
                            }
                        }
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putFloat("souls", souls);
        tag.putInt("progress", craftProgress);
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
        craftProgress = nbt.getInt("progress");
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
        SimpleContainer inventory = new SimpleContainer(focusItemHandler.getSlots());
        for (int i = 0; i < focusItemHandler.getSlots(); i++) {
            inventory.setItem(i, focusItemHandler.getStackInSlot(i));
        }
        return inventory;
    }
    float explosionTimer = 0;
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SoulAltarBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            ItemStack focusSlot = pBlockEntity.focusItemHandler.getStackInSlot(0);
            Optional<SoulAltarRecipe> match = pLevel.getRecipeManager()
                    .getRecipeFor(SoulAltarRecipe.Type.INSTANCE, pBlockEntity.getInv(), pLevel);
            if (match.isPresent()) {
                if (pBlockEntity.craftProgress > match.get().getIngredients().size()) {
                    ItemStack stack = match.get().getResultItem(RegistryAccess.EMPTY);
                    ItemEntity entity = new ItemEntity(pLevel, (float)pPos.getX()+0.5f, (float)pPos.getY()+1f, (float)pPos.getZ()+0.5f, stack);
                    pLevel.addFreshEntity(entity);
                    pBlockEntity.item = ItemStack.EMPTY;
                    pBlockEntity.craftProgress = 0;
                    pBlockEntity.focusItemHandler.extractItem(0, 1, false);
                    pBlockEntity.explosionTimer = 0;
                } else {
                    pBlockEntity.item = match.get().getIngredients().get(pBlockEntity.craftProgress-1).getItems()[0];
                    if (pBlockEntity.getSouls() > 0) {
                        pBlockEntity.setSouls(pBlockEntity.getSouls() - 0.1f);
                    } else {
                        pBlockEntity.explosionTimer++;
                        ((ServerLevel)pLevel).sendParticles(ParticleInit.SOUL.get(), (float)pPos.getX()+0.5f, (float)pPos.getY()+0.5f, (float)pPos.getZ()+0.5f, 25, 0, 0, 0, 0.75f);
                        if (pBlockEntity.explosionTimer >= 25) {
                            pBlockEntity.explosionTimer = 0;
                            pBlockEntity.craftProgress = 0;
                            pBlockEntity.focusItemHandler.extractItem(0, 1, false);
                            ((ServerLevel) pLevel).sendParticles(ParticleTypes.EXPLOSION_EMITTER, pPos.getX(), pPos.getY(), pPos.getZ(), 1, 0, 0, 0, 0);
                            pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
                            List<LivingEntity> entities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, null, AABB.ofSize(pPos.getCenter(), 5f, 5f, 5f));
                            for (LivingEntity p : entities) {
                                p.hurt(pLevel.damageSources().source(RunicArts.soulExplosion), 10);
                            }
                            pBlockEntity.item = ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!focusSlot.is(ItemStack.EMPTY.getItem())) {
                pBlockEntity.focusItemHandler.extractItem(0, 1, false);
                pBlockEntity.craftProgress = 0;
                pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 2, 1);
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
        event.getController().setAnimation(RawAnimation.begin().then("animation.soulaltar.idle", Animation.LoopType.LOOP));
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
}
