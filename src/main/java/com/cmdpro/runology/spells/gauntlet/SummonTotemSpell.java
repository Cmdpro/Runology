package com.cmdpro.runology.spells.gauntlet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
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
        player.sendSystemMessage(Component.literal("test"));
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
