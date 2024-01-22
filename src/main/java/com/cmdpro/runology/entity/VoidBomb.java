package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VoidBomb extends BillboardProjectile {
    public int time;
    public VoidBomb(EntityType<VoidBomb> entityType, Level world) {
        super(entityType, world);
    }
    protected VoidBomb(EntityType<VoidBomb> entityType, double x, double y, double z, Level world) {
        this(entityType, world);
        this.setPos(x, y, z);

    }
    public VoidBomb(EntityType<VoidBomb> entityType, LivingEntity shooter, Level world) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
        this.setOwner(shooter);
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        hitAnything(hit);
    }
    public float minExplodeHeight;
    public void hitAnything(HitResult hit) {
        ((ServerLevel)level()).sendParticles(ParticleTypes.EXPLOSION_EMITTER, position().x, position().y, position().z, 1, 0, 0, 0, 0f);
        level().playSound(null, position().x, position().y, position().z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0f, 1.0f);
        List<Entity> entities = level().getEntities(this, AABB.ofSize(position(), 5f, 5f, 5f));
        for (Entity p : entities) {
            if (!(p instanceof RunicOverseer)) {
                p.hurt(level().damageSources().mobAttack((LivingEntity) this.getOwner()), 20);
            }
        }
        Iterable<BlockPos> blocks = BlockPos.betweenClosed(blockPosition().offset(-3, -3, -3), blockPosition().offset(3, 3, 3));
        for (BlockPos i : blocks) {
            if (i.getY() >= Math.floor(minExplodeHeight) && !level().getBlockState(i).is(BlockInit.MYSTERIOUSALTAR.get())) {
                level().destroyBlock(i, false);
            }
        }
        remove(RemovalReason.KILLED);
    }

    @Override
    public ResourceLocation getSprite() {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/voidbomb.png");
    }
    @Override
    public float getScale() {
        return 1;
    }

    @Override
    protected void defineSynchedData() {

    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("xd", (float)this.getDeltaMovement().x);
        tag.putFloat("yd", (float)this.getDeltaMovement().y);
        tag.putFloat("zd", (float)this.getDeltaMovement().z);
        tag.putInt("time", (int)this.time);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        float vx = tag.getFloat("xd");
        float vy = tag.getFloat("yd");
        float vz = tag.getFloat("zd");
        this.time = tag.getInt("time");
        setDeltaMovement(new Vec3(vx, vy, vz));
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }
    public float gravity;
    @Override
    public void tick() {
        gravity += 0.05;
        if (gravity >= 0.25) {
            gravity = 0.25f;
        }
        setDeltaMovement(getDeltaMovement().add(0, -gravity, 0));
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
            damagesource = entity.damageSources().mobAttack((LivingEntity)entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else if (entity instanceof Player) {
            damagesource = entity.damageSources().playerAttack((Player)entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else {
            damagesource = null;
        }
        if (damagesource != null) {
            hitAnything(hit);
        }
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
