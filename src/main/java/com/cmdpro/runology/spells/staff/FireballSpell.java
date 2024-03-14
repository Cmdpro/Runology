package com.cmdpro.runology.spells.staff;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.entity.FireballProjectile;
import com.cmdpro.runology.entity.VoidBomb;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;

import java.util.HashMap;

public class FireballSpell extends Spell {
    @Override
    public int magicLevel() {
        return 1;
    }

    @Override
    public HashMap<ResourceLocation, Float> getCost() {
        return new HashMap<>() {
            {
                put(new ResourceLocation(Runology.MOD_ID, "fire"), 10f);
            }
        };
    }

    @Override
    public boolean cast(Player player, boolean fromStaff, boolean fromGauntlet) {
        FireballProjectile fireball = new FireballProjectile(EntityInit.FIREBALL.get(), player, player.level());
        player.level().addFreshEntity(fireball);
        return true;
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
