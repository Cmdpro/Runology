package com.cmdpro.runicarts.entity;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.SoulcasterEffect;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
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

public class SoulRitualController extends Entity {
    public int time;
    public SoulRitualController(EntityType<SoulRitualController> entityType, Level world) {
        super(entityType, world);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("time", time);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        time = tag.getInt("time");
    }

    @Override
    protected void defineSynchedData() {

    }
    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes().build();
    }

    public void soulEffect(Vec3 pos) {
        ((ServerLevel)level()).sendParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 15, 0, 0, 0, 0.1);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 1, 1);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            Multiblock ritual = MultiblockDataManager.get().getMultiblock(new ResourceLocation(RunicArts.MOD_ID, "soulritualnoflames"));
            if (
                    !ritual.validate(level(), blockPosition().below(), Rotation.NONE) &&
                            !ritual.validate(level(), blockPosition().below(), Rotation.CLOCKWISE_90) &&
                            !ritual.validate(level(), blockPosition().below(), Rotation.CLOCKWISE_180) &&
                            !ritual.validate(level(), blockPosition().below(), Rotation.COUNTERCLOCKWISE_90)
            ) {
                remove(RemovalReason.DISCARDED);
            }
            time++;
            if (time == 20) {
                Vec3 pos = blockPosition().getCenter().subtract(0, 0.5f, 0);
                soulEffect(pos);
                level().removeBlock(blockPosition(), false);
            }
            if (time == 40) {
                Vec3 pos = blockPosition().offset(4, 0, 0).getCenter().subtract(0, 0.5f, 0);
                soulEffect(pos);
                level().removeBlock(blockPosition().offset(4, 0, 0), false);
            }
            if (time == 60) {
                Vec3 pos = blockPosition().offset(-4, 0, 0).getCenter().subtract(0, 0.5f, 0);
                soulEffect(pos);
                level().removeBlock(blockPosition().offset(-4, 0, 0), false);
            }
            if (time == 80) {
                Vec3 pos = blockPosition().offset(0, 0, 4).getCenter().subtract(0, 0.5f, 0);
                soulEffect(pos);
                level().removeBlock(blockPosition().offset(0, 0, 4), false);
            }
            if (time == 100) {
                Vec3 pos = blockPosition().offset(0, 0, -4).getCenter().subtract(0, 0.5f, 0);
                soulEffect(pos);
                level().removeBlock(blockPosition().offset(0, 0, -4), false);
            }
            if (time == 150) {
                RunicArtsUtil.spawnSoulKeeper(blockPosition().getCenter().add(0, 4, 0), level());
            }
            if (time >= 150) {
                remove(RemovalReason.DISCARDED);
            }
        }
        super.tick();
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
