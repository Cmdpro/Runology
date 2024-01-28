package com.cmdpro.runology.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public interface InstabilityEventRunnable {
    public default void run(Player player, LevelChunk chunk) {}
}