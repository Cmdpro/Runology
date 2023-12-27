package com.cmdpro.runicarts.soulcastereffects;

import com.cmdpro.runicarts.api.SoulcasterEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class EnderSoulcasterEffect extends SoulcasterEffect {
    public EnderSoulcasterEffect() {
        soulCost = 5;
        color = new Color(1f, 0f, 1f);
    }

    @Override
    public void hitGround(LivingEntity caster, Vec3 pos, Level level, int amount) {
        super.hitGround(caster, pos, level, amount);
        caster.level().playSound(null, new BlockPos((int)pos.x, (int)pos.y, (int)pos.z), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
        if (caster.level() != level) {
            caster.changeDimension((ServerLevel) level);
        }
        caster.teleportTo(pos.x, pos.y, pos.z);
        level.playSound(null, new BlockPos((int)pos.x, (int)pos.y, (int)pos.z), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void hitEntity(LivingEntity caster, LivingEntity victim, int amount) {
        super.hitEntity(caster, victim, amount);
        Vec3 pos = victim.position();
        Level level = victim.level();
        caster.level().playSound(null, new BlockPos((int)pos.x, (int)pos.y, (int)pos.z), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
        if (caster.level() != level) {
            caster.changeDimension((ServerLevel) level);
        }
        caster.teleportTo(pos.x, pos.y, pos.z);
        level.playSound(null, new BlockPos((int)pos.x, (int)pos.y, (int)pos.z), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }
}
