package com.cmdpro.runology.spells.gauntlet;

import com.cmdpro.runology.api.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class SummonTotemSpell extends Spell {
    @Override
    public int magicLevel() {
        return 1;
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
