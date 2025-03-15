package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.particle.ShatterParticle;
import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;

public class RunicCodexEntry extends Entity {
    public RunicCodex codex;
    public RunicCodexEntry(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public RunicCodexEntry(Level level) {
        super(EntityRegistry.RUNIC_CODEX_ENTRY.get(), level);
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (codex != null) {
            codex.entryEntities.remove(this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            ClientHandler.spawnParticles(level(), getBoundingBox().getCenter());
        } else {
            if (codex != null) {
                if (((ServerLevel)level()).getEntity(codex.getUUID()) == null) {
                    remove(RemovalReason.DISCARDED);
                }
            } else {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
    private static class ClientHandler {
        public static void spawnParticles(Level level, Vec3 position) {
            if (Minecraft.getInstance().level == level) {
                Minecraft.getInstance().particleEngine.createParticle(ParticleRegistry.SHATTER.get(), position.x + RandomUtils.nextFloat(0f, 0.2f) - 0.1f, position.y + RandomUtils.nextFloat(0f, 0.2f) - 0.1f, position.z + RandomUtils.nextFloat(0f, 0.2f) - 0.1f, RandomUtils.nextFloat(0, 0.1f) - 0.05f, RandomUtils.nextFloat(0.1f, 0.2f), RandomUtils.nextFloat(0, 0.1f) - 0.05f);
            }
        }
    }
}
