package com.cmdpro.runology.data.entries;

import com.cmdpro.runology.api.guidebook.Page;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Entry {

    public Entry(ResourceLocation id, ResourceLocation tab, ItemLike icon, double x, double y, double z, List<Page> pages, List<ResourceLocation> parents, Component name, Optional<ResourceLocation> advancement) {
        this.id = id;
        this.icon = new ItemStack(icon);
        this.pos = new Vec3(x, y, z);
        this.pages = pages;
        this.parents = parents;
        this.name = name;
        this.tab = tab;
        this.advancement = advancement;
    }
    public Entry(ResourceLocation id, ResourceLocation tab, ItemStack icon, double x, double y, double z, List<Page> pages, List<ResourceLocation> parents, Component name, Optional<ResourceLocation> advancement) {
        this.id = id;
        this.icon = icon;
        this.pos = new Vec3(x, y, z);
        this.pages = pages;
        this.parents = parents;
        this.name = name;
        this.tab = tab;
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
        for (Entry i : getParentEntries()) {
            if (!i.isUnlocked(player)) {
                unlocked = false;
                break;
            }
        }
        return unlocked;
    }
    public ResourceLocation tab;
    public Entry[] getParentEntries() {
        return parentEntries;
    }
    public void setParentEntries(List<ResourceLocation> parents) {
        this.parents = parents;
        updateParentEntries();
    }
    public boolean updateParentEntries() {
        List<Entry> parentEntries = new ArrayList<>();
        for (ResourceLocation i : parents) {
            if (EntryManager.entries.containsKey(i)) {
                parentEntries.add(EntryManager.entries.get(i));
            }
        }
        this.parentEntries = parentEntries.toArray(new Entry[0]);
        return !parentEntries.isEmpty();
    }
    public Component name;
    public ItemStack icon;
    public ResourceLocation id;
    public Vec3 pos;
    public List<Page> pages;
    public List<ResourceLocation> parents;
    private Entry[] parentEntries;
    public Optional<ResourceLocation> advancement;
}