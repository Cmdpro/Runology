package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.DisplayEnderTransporterParticleLineS2CPacket;
import com.cmdpro.runology.networking.packet.DisplayPrismaticBulletEffectsS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import javax.annotation.Nullable;
import java.awt.*;

public class PrismaticBulletProjectile extends Projectile {
    public int time;
    public PrismaticBulletProjectile(EntityType<PrismaticBulletProjectile> entityType, Level world) {
        super(entityType, world);
    }
    protected PrismaticBulletProjectile(EntityType<PrismaticBulletProjectile> entityType, double x, double y, double z, Level world) {
        this(entityType, world);
        this.setPos(x, y, z);
    }
    public PrismaticBulletProjectile(EntityType<PrismaticBulletProjectile> entityType, LivingEntity shooter, Level world) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
        this.setOwner(shooter);
        this.setDeltaMovement(shooter.getLookAngle().multiply(0.5, 0.5, 0.5));
    }
    @Override
    protected void onHitBlock(BlockHitResult hit) {
        setPos(hit.getLocation());
        playSound(SoundEvents.ITEM_FRAME_ADD_ITEM);
        displayPop();
        remove(RemovalReason.KILLED);
        super.onHitBlock(hit);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("time", (int)this.time);
        tag.putFloat("red", this.color.getRed());
        tag.putFloat("green", this.color.getGreen());
        tag.putFloat("blue", this.color.getBlue());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.time = tag.getInt("time");
        this.color = new Color(tag.getFloat("red"), tag.getFloat("green"), tag.getFloat("blue"));
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    public static final EntityDataAccessor<Vector3f> COLOR = SynchedEntityData.defineId(PrismaticBulletProjectile.class, EntityDataSerializers.VECTOR3);
    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR, new Vector3f(0, 0, 0));
    }
    Vec3 previousPos;
    public Color color;
    @Override
    public void tick() {
        previousPos = position();
        if (!this.isNoGravity() && !this.noPhysics) {
            setDeltaMovement(getDeltaMovement().add(0, -0.05f, 0));
        }
        this.setPos(this.getX() + getDeltaMovement().x, this.getY() + getDeltaMovement().y, this.getZ() + getDeltaMovement().z);
        if (!level().isClientSide) {
            entityData.set(COLOR, new Vector3f((float)color.getRed()/255f, (float)color.getGreen()/255f, (float)color.getBlue()/255f));
            time++;
            if (time >= 100) {
                remove(RemovalReason.KILLED);
            }
            displayTrail();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            while (!isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(this.position(), this.position().add(getDeltaMovement()));
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) hitresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if ((entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity))) {
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
        super.tick();
    }

    public void displayPop() {
        Vec3 pos = position().add(0, getBoundingBox().getYsize() / 2f, 0);
        DisplayPrismaticBulletEffectsS2CPacket packet = new DisplayPrismaticBulletEffectsS2CPacket(pos, color, 1);
        for(int j = 0; j < ((ServerLevel)level()).players().size(); ++j) {
            ServerPlayer serverplayer = ((ServerLevel)level()).players().get(j);
            if (serverplayer.level() == ((ServerLevel)level())) {
                BlockPos blockpos = serverplayer.blockPosition();
                if (blockpos.closerToCenterThan(pos, 32.0D)) {
                    ModMessages.sendToPlayer(packet, serverplayer);
                }
            }
        }
    }
    public void displayTrail() {
        for (double i = 0; i < 3; i++) {
            Vec3 pos = position();
            pos = pos.subtract(getDeltaMovement().x*(i/3d), getDeltaMovement().y*(i/3d), getDeltaMovement().z*(i/3d));
            pos = pos.add(0, getBoundingBox().getYsize() / 2f, 0);
            DisplayPrismaticBulletEffectsS2CPacket packet = new DisplayPrismaticBulletEffectsS2CPacket(pos, color, 0);
            for(int j = 0; j < ((ServerLevel)level()).players().size(); ++j) {
                ServerPlayer serverplayer = ((ServerLevel)level()).players().get(j);
                if (serverplayer.level() == ((ServerLevel)level())) {
                    BlockPos blockpos = serverplayer.blockPosition();
                    if (blockpos.closerToCenterThan(pos, 32.0D)) {
                        ModMessages.sendToPlayer(packet, serverplayer);
                    }
                }
            }
        }
    }

    public Color getColor() {
        Vector3f color = entityData.get(COLOR);
        return new Color(color.x, color.y, color.z);
    }
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        Entity entity = this.getOwner();
        DamageSource damagesource;
        if (entity instanceof LivingEntity) {
            damagesource = this.damageSources().source(Runology.magicProjectile, this, entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else if (entity instanceof Player) {
            damagesource = this.damageSources().source(Runology.magicProjectile, this, entity);
            ((LivingEntity)entity).setLastHurtMob(hit.getEntity());
        } else {
            damagesource = null;
        }
        boolean flag = hit.getEntity().getType() == EntityType.ENDERMAN;
        if (damagesource != null) {
            if (hit.getEntity() instanceof LivingEntity ent && entity instanceof LivingEntity) {
                if (ent.hurt(damagesource, 10)) {
                    if (flag) {
                        return;
                    }
                }
            }
        }
        setPos(position().add(getDeltaMovement()));
        displayTrail();
        playSound(SoundEvents.ITEM_FRAME_ADD_ITEM);
        displayPop();
        remove(RemovalReason.KILLED);
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
