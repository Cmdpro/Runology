package com.cmdpro.runology.spells.gauntlet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class ConjureSparkSpell extends Spell {
    @Override
    public int magicLevel() {
        return 1;
    }

    @Override
    public HashMap<ResourceLocation, Float> getCost() {
        return new HashMap<>() {
            {
                put(new ResourceLocation(Runology.MOD_ID, "energy"), 10f);
            }
        };
    }

    @Override
    public boolean cast(Player player, boolean fromStaff, boolean fromGauntlet) {
        double d0 = (double) player.getPickRadius();
        double entityReach = player.getEntityReach();
        HitResult hitResult = player.pick(Math.max(d0, entityReach), 1, false);
        if (hitResult instanceof BlockHitResult result) {
            BlockPos pos = result.getBlockPos().relative(result.getDirection());
            if (player.level().getBlockState(pos).canBeReplaced() && !player.level().getBlockState(result.getBlockPos()).isAir()) {
                player.level().setBlockAndUpdate(pos, BlockInit.SPARK.get().defaultBlockState());
                player.level().playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1, 2);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean gauntletCastable() {
        return true;
    }

    @Override
    public boolean staffCastable() {
        return false;
    }
}
