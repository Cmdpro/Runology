package com.cmdpro.runology.data.entries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class EntryTab {

    public EntryTab(ResourceLocation id, ItemLike icon, Component name, int priority, Optional<ResourceLocation> advancement) {
        this.id = id;
        this.icon = new ItemStack(icon);
        this.name = name;
        this.priority = priority;
        this.advancement = advancement;
    }
    public EntryTab(ResourceLocation id, ItemStack icon, Component name, int priority, Optional<ResourceLocation> advancement) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.priority = priority;
        this.advancement = advancement;
    }
    public boolean isUnlocked(ServerPlayer player) {
        boolean unlocked = false;
        if (advancement.isPresent()) {
            var holder = player.getServer().getAdvancements().get(advancement.get());
            if (holder != null) {
                if (player.getAdvancements().getOrStartProgress(holder).isDone()) {
                    unlocked = true;
                }
            }
        } else {
            unlocked = true;
        }
        return unlocked;
    }
    public ResourceLocation id;
    public Component name;
    public ItemStack icon;
    public int priority;
    public Optional<ResourceLocation> advancement;
}
