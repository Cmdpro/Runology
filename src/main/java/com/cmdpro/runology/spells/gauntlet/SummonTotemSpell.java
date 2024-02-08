package com.cmdpro.runology.spells.gauntlet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.entity.Totem;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class SummonTotemSpell extends Spell {
    @Override
    public int magicLevel() {
        return 1;
    }

    @Override
    public HashMap<ResourceLocation, Float> getCost() {
        return new HashMap<>() {
            {
                put(new ResourceLocation(Runology.MOD_ID, "earth"), 100f);
            }
        };
    }

    @Override
    public void cast(Player player, boolean fromStaff, boolean fromGauntlet) {
        Totem totem = new Totem(EntityInit.TOTEM.get(), player.level());
        totem.setPos(player.position());
        player.level().addFreshEntity(totem);
        totem.triggerAnim("attackController", "animation.totem.spawning");
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
