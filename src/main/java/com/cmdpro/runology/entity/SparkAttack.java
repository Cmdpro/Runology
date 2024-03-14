package com.cmdpro.runology.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class SparkAttack extends Entity {
    public int time;
    public SparkAttack(EntityType<SparkAttack> entityType, Level world) {
        super(entityType, world);
    }
    protected SparkAttack(EntityType<SparkAttack> entityType, Vec3 pos, Level world) {
        this(entityType, world);
        this.setPos(pos);

    }
    public SparkAttack(EntityType<SparkAttack> entityType, Level world, Vec3 pos, Entity victim) {
        this(entityType, pos, world);
        this.victimPos = victim.getBoundingBox().getCenter().toVector3f();
        entityData.set(VICTIMPOS, victimPos);
    }
    public SparkAttack(EntityType<SparkAttack> entityType, Level world, Vec3 pos, Vec3 victimPos) {
        this(entityType, pos, world);
        this.victimPos = victimPos.toVector3f();
        entityData.set(VICTIMPOS, this.victimPos);
    }
    public HashMap<Float, Vec2> offsets = new HashMap<>();

    public Vector3f victimPos;
    public static final EntityDataAccessor<Vector3f> VICTIMPOS = SynchedEntityData.defineId(SparkAttack.class, EntityDataSerializers.VECTOR3);
    @Override
    protected void defineSynchedData() {
        this.entityData.define(VICTIMPOS, new Vector3f());
    }

    @Override
    public void tick() {
        super.tick();
        time++;
        if (!level().isClientSide) {
            if (time >= 20) {
                remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey.equals(VICTIMPOS)) {
            Vector3f vector3f = entityData.get(VICTIMPOS);
            if (vector3f != null) {
                Vec3 pos = new Vec3(vector3f.x, vector3f.y, vector3f.z);
                double distance = pos.distanceTo(position());
                int amount = RandomUtils.nextInt(3, 8);
                offsets.clear();
                for (int i = 0; i < amount; i++) {
                    offsets.put(RandomUtils.nextFloat(0, (float)distance), new Vec2(RandomUtils.nextFloat(0, 360)-180f, RandomUtils.nextFloat(0, 0.8f) - 0.4f));
                }
            }
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
