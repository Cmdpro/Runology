package com.cmdpro.runology.data.entries;

import com.cmdpro.runology.Runology;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class EntryTabManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static EntryTabManager instance;
    protected EntryTabManager() {
        super(GSON, "runology/guidebook/tabs");
    }
    public static EntryTabManager getOrCreateInstance() {
        if (instance == null) {
            instance = new EntryTabManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, EntryTab> tabs = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        tabs.clear();
        Runology.LOGGER.info("Adding Runology Tabs");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                EntryTab tab = deserializeTab(location, GsonHelper.convertToJsonObject(i.getValue(), "top member"));
                if (tab == null) {
                    continue;
                }
                tabs.put(i.getKey(), tab);
                Runology.LOGGER.info("Successfully added tab {}", location);
            } catch (IllegalArgumentException | JsonParseException e) {
                Runology.LOGGER.error("Parsing error loading tab {}", location, e);
            }
        }
        Runology.LOGGER.info("Loaded {} Runology Tabs", tabs.size());
    }
    public static EntryTabSerializer serializer = new EntryTabSerializer();
    protected EntryTab deserializeTab(ResourceLocation id, JsonObject json) {
        EntryTabSerializer serializer = this.serializer;
        if (serializer != null) {
            return serializer.read(id, json);
        }
        return null;
    }
}
