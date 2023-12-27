package com.cmdpro.runicarts.entity;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.IAmplifierSoulcastersCrystal;
import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.api.SoulcasterEffect;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.soulcastereffects.IceSoulcasterEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpellProjectile extends BillboardProjectile {
    public int time;
    public boolean fromBoss;
    public SpellProjectile(EntityType<SpellProjectile> entityType, Level world) {
        super(entityType, world);
    }
    protected SpellProjectile(EntityType<SpellProjectile> entityType, double x, double y, double z, Level world) {
        this(entityType, world);
        this.setPos(x, y, z);
    }
    public SpellProjectile(EntityType<SpellProjectile> entityType, LivingEntity shooter, Level world) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
        this.setOwner(shooter);
    }
    @Override
    public float getOffset() {
        return 0.25f;
    }
    @Override
    protected void onHitBlock(BlockHitResult hit) {
        if (getOwner() instanceof LivingEntity) {
            HashMap<SoulcasterEffect, Integer> effects2 = new HashMap<>();
            for (SoulcasterEffect i : effects) {
                if (!effects2.containsKey(i)) {
                    effects2.put(i, 1);
                } else {
                    effects2.put(i, effects2.get(i)+1);
                }
            }
            effects2.forEach((k, v) -> {
                k.hitGround((LivingEntity)getOwner(), position(), level(), v);
            });
        }
        remove(RemovalReason.KILLED);
        super.onHitBlock(hit);
    }

    @Override
    public ResourceLocation getSprite() {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/entity/spell.png");
    }
    @Override
    public float getScale() {
        return 0.5f;
    }

    private static final EntityDataAccessor<CompoundTag> COLOR = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.COMPOUND_TAG);
    @Override
    protected void defineSynchedData() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("r", 1);
        tag.putFloat("g", 1);
        tag.putFloat("b", 1);
        tag.putFloat("a", 0);
        this.entityData.define(COLOR, tag);
    }
    public List<SoulcasterEffect> effects = new ArrayList<>();
    public int amplifier = 1;
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("xd", (float)this.getDeltaMovement().x);
        tag.putFloat("yd", (float)this.getDeltaMovement().y);
        tag.putFloat("zd", (float)this.getDeltaMovement().z);
        tag.putInt("time", (int)this.time);
        ListTag tag2 = new ListTag();
        for (SoulcasterEffect i : effects) {
            CompoundTag tag3 = new CompoundTag();
            tag3.putString("id", RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY.get().getKey(i).toString());
            tag2.add(tag3);
        }
        tag.put("types", tag2);
        tag.putInt("amplifier", amplifier);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        float vx = tag.getFloat("xd");
        float vy = tag.getFloat("yd");
        float vz = tag.getFloat("zd");
        this.time = tag.getInt("time");
        setDeltaMovement(new Vec3(vx, vy, vz));
        ListTag types = (ListTag)tag.get("types");
        List<SoulcasterEffect> effects = new ArrayList<>();
        for (Tag i : types) {
            CompoundTag i2 = (CompoundTag) i;
            SoulcasterEffect effect = RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY.get().getValue(ResourceLocation.of(i2.getString("id"), ':'));
            effects.add(effect);
        }
        amplifier = tag.getInt("amplifier");
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    public Color getColor() {
        return color;
    }
    public static Color blend(Color c0, Color c1) {
        double totalAlpha = c0.getAlpha() + c1.getAlpha();
        double weight0 = c0.getAlpha() / totalAlpha;
        double weight1 = c1.getAlpha() / totalAlpha;

        double r = weight0 * c0.getRed() + weight1 * c1.getRed();
        double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
        double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
        double a = Math.max(c0.getAlpha(), c1.getAlpha());

        return new Color((int) r, (int) g, (int) b, (int) a);
    }
    public Color color = new Color(1f, 1f, 1f, 0f);
    public CompoundTag colorTag = new CompoundTag();
    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (effects.size() > 0) {
                color = effects.get(0).color;
                for (SoulcasterEffect i : effects) {
                    color = blend(color, i.color);
                }
            }
            colorTag.putFloat("r", ((float)color.getRed())/255f);
            colorTag.putFloat("g", ((float)color.getGreen())/255f);
            colorTag.putFloat("b", ((float)color.getBlue())/255f);
            colorTag.putFloat("a", ((float)color.getAlpha())/255f);
            entityData.set(COLOR, colorTag);
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

        } else {
            colorTag = entityData.get(COLOR);
            if (colorTag.contains("r") && colorTag.contains("g") && colorTag.contains("b") && colorTag.contains("a")) {
                color = new Color(colorTag.getFloat("r"),
                        colorTag.getFloat("g"),
                        colorTag.getFloat("b"),
                        colorTag.getFloat("a"));
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
            if (hit.getEntity() instanceof LivingEntity && entity instanceof LivingEntity) {
                HashMap<SoulcasterEffect, Integer> effects2 = new HashMap<>();
                for (SoulcasterEffect i : effects) {
                    if (!effects2.containsKey(i)) {
                        effects2.put(i, 1);
                    } else {
                        effects2.put(i, effects2.get(i)+1);
                    }
                }
                effects2.forEach((k, v) -> {
                    k.hitEntity((LivingEntity)entity, (LivingEntity)hit.getEntity(), v);
                });
            }
        }
        remove(RemovalReason.KILLED);
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
