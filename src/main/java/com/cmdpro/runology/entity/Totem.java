package com.cmdpro.runology.entity;

import com.cmdpro.runology.ShatterRealmTeleporter;
import com.cmdpro.runology.api.EmpoweredGauntlet;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.DimensionInit;
import com.cmdpro.runology.init.TagInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Totem extends LivingEntity implements GeoEntity {
    public Totem(EntityType<Totem> entityType, Level world) {
        super(entityType, world);
        blocksBuilding = true;
    }
    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D).build();
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    public int lifeTime;


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            for (Player i : level().getEntitiesOfClass(Player.class, AABB.ofSize(position(), 10, 10, 10))) {
                i.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 2));
            }
            lifeTime++;
            if (lifeTime >= 500) {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        lifeTime = pCompound.getInt("lifeTime");
    }
    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public Iterable<ItemStack> getHandSlots() {
        return this.handItems;
    }

    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        switch (pSlot.getType()) {
            case HAND:
                return this.handItems.get(pSlot.getIndex());
            case ARMOR:
                return this.armorItems.get(pSlot.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        this.verifyEquippedItem(pStack);
        switch (pSlot.getType()) {
            case HAND:
                this.onEquipItem(pSlot, this.handItems.set(pSlot.getIndex(), pStack), pStack);
                break;
            case ARMOR:
                this.onEquipItem(pSlot, this.armorItems.set(pSlot.getIndex(), pStack), pStack);
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("lifeTime", lifeTime);
    }

    //@Override
    //public boolean isNoGravity() {
    //    return true;
    //}
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.totem.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private <E extends GeoAnimatable> PlayState openPredicate(AnimationState event) {
        return PlayState.CONTINUE;
    }
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
        data.add(new AnimationController(this, "attackController", 0, this::openPredicate)
                .triggerableAnim("animation.totem.spawning", RawAnimation.begin().then("animation.totem.spawning", Animation.LoopType.PLAY_ONCE))
                .setSoundKeyframeHandler(state -> {
                    level().playLocalSound(position().x, position().y, position().z, SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1, 1, false);
                }));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
