package com.cmdpro.runology.spells.staff;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.entity.FireballProjectile;
import com.cmdpro.runology.entity.IceShard;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;

public class IceShardsSpell extends Spell {
    @Override
    public int magicLevel() {
        return 1;
    }

    @Override
    public HashMap<ResourceLocation, Float> getCost() {
        return new HashMap<>() {
            {
                put(new ResourceLocation(Runology.MOD_ID, "ice"), 10f);
            }
        };
    }

    @Override
    public void cast(Player player, boolean fromStaff, boolean fromGauntlet) {
        for (int i = 0; i < 15; i++) {
            IceShard shard = new IceShard(player.level(), player);
            float xDir = player.getXRot()+RandomUtils.nextFloat(0f, 30f)-15f;
            float yDir = player.getYHeadRot()+RandomUtils.nextFloat(0f, 60f)-30f;
            shard.setDeltaMovement(calculateViewVector(xDir, yDir));
            player.level().addFreshEntity(shard);
        }
    }
    protected final Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    @Override
    public boolean gauntletCastable() {
        return false;
    }

    @Override
    public boolean staffCastable() {
        return true;
    }
}
