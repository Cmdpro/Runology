package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import java.util.ArrayList;
import java.util.List;

public class VoidBullet extends Projectile implements GeoEntity {
    public int time;
    public VoidBullet(EntityType<VoidBullet> entityType, Level world) {
        super(entityType, world);
    }
    protected VoidBullet(EntityType<VoidBullet> entityType, double x, double y, double z, Level world) {
        this(entityType, world);
        this.setPos(x, y, z);

    }
    public VoidBullet(EntityType<VoidBullet> entityType, LivingEntity shooter, Level world) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
        this.setOwner(shooter);
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        remove(RemovalReason.KILLED);
    }

    @Override
    protected void defineSynchedData() {

    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("time", (int)this.time);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.time = tag.getInt("time");
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }
    @Override
    public void tick() {
        if (!level().isClientSide) {
            time++;
            if (time >= 100) {
                remove(RemovalReason.KILLED);
            }
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            while(!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(this.position(), this.position().add(getDeltaMovement()));
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult)hitresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if ((entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity))) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                    this.onHit(hitresult);
                    this.hasImpulse = true;
                }

                if (entityhitresult == null) {
                    break;
                }

                hitresult = null;
            }
        }
        this.setPos(this.getX() + getDeltaMovement().x, this.getY() + getDeltaMovement().y, this.getZ() + getDeltaMovement().z);
        super.tick();
    }
    //@Override
    //public boolean isNoGravity() {
    //    return true;
    //}

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        Entity entity = this.getOwner();
        DamageSource damagesource;
        if (entity instanceof LivingEntity) {
            damagesource = entity.damageSources().source(Runology.magicProjectile, this, (LivingEntity)entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else if (entity instanceof Player) {
            damagesource = entity.damageSources().source(Runology.magicProjectile, this, (Player)entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else {
            damagesource = null;
        }
        if (damagesource != null) {
            hit.getEntity().hurt(damagesource, 25);
        }
        remove(RemovalReason.KILLED);
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.voidbullet.move", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
