package com.cmdpro.runology.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public abstract class SpellcastingUpgrade {
    public HashMap<ResourceLocation, Float> costChanges(HashMap<ResourceLocation, Float> cost) { return cost; }
    public void tick(Player player, ItemStack stack) {}
}
