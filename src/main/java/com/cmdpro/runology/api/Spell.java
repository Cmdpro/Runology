package com.cmdpro.runology.api;

import net.minecraft.world.entity.player.Player;

public abstract class Spell {
    public abstract int magicLevel();
    public abstract void cast(Player player, boolean fromStaff, boolean fromGauntlet);
    public abstract boolean gauntletCastable();
    public abstract boolean staffCastable();
}
