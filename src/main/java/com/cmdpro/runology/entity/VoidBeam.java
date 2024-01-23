package com.cmdpro.runology.entity;

import com.cmdpro.runology.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VoidBeam extends Entity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    public Player player;
    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }

    }
    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }
    public int time;
    public VoidBeam(EntityType<VoidBeam> entityType, Level world) {
        super(entityType, world);
    }
    protected VoidBeam(EntityType<VoidBeam> entityType, double x, double y, double z, Level world) {
        this(entityType, world);
        this.setPos(x, y, z);

    }
    public VoidBeam(EntityType<VoidBeam> entityType, LivingEntity shooter, Level world) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
        this.setOwner(shooter);
    }
    public static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(VoidBeam.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PLAYER, Optional.empty());
    }
    @Override
    public void tick() {
        super.tick();
        time++;
        if (!level().isClientSide) {
            if (player == null) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            entityData.set(PLAYER, Optional.of(player.getUUID()));
            if (time == 110) {
                playSound(SoundEvents.BEACON_POWER_SELECT);
                for (Player i : level().players()) {
                    if (i.position().x >= position().x-0.5 && i.position().z >= position().z-0.5 && i.position().x <= position().x+0.5 && i.position().z <= position().z+0.5 && i.position().y >= position().y) {
                        i.hurt(damageSources().mobAttack((LivingEntity) getOwner()), 40);
                    }
                }
            }
            if (time >= 120) {
                remove(RemovalReason.KILLED);
            }
        }
        if (level().isClientSide) {
            Optional<UUID> plr = entityData.get(PLAYER);
            if (plr.isPresent()) {
                player = level().getPlayerByUUID(plr.get());
            }
        }
        if (player == null) {
            return;
        }
        if (time <= 100) {
            Vec3 pos = new Vec3(player.position().x, player.blockPosition().getCenter().y, player.position().z);
            while (!level().getBlockState(BlockPos.containing(pos)).isSolid() && pos.y > level().getMinBuildHeight()) {
                pos = pos.add(0, -1, 0);
            }
            pos = pos.add(0, 0.5f, 0);
            setPos(pos);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    //@Override
    //public boolean isNoGravity() {
    //    return true;
    //}
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
