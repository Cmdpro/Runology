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

public class EntryManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static EntryManager instance;
    protected EntryManager() {
        super(GSON, "runology/guidebook/entries");
    }
    public static EntryManager getOrCreateInstance() {
        if (instance == null) {
            instance = new EntryManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, Entry> entries = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        entries.clear();
        Runology.LOGGER.info("Adding Runology Entries");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                Entry tab = deserializeEntry(location, GsonHelper.convertToJsonObject(i.getValue(), "top member"));
                if (tab == null) {
                    Runology.LOGGER.info("Skipping loading element {} as its serializer returned null", location);
                    continue;
                }
                entries.put(i.getKey(), tab);
            } catch (IllegalArgumentException | JsonParseException e) {
                Runology.LOGGER.error("Parsing error loading entry {}", location, e);
            }
        }
        for (Entry i : entries.values()) {
            i.updateParentEntries();
        }
        Runology.LOGGER.info("Loaded {} entries", entries.size());
    }
    public static EntrySerializer serializer = new EntrySerializer();
    protected Entry deserializeEntry(ResourceLocation id, JsonObject json) {
        EntrySerializer serializer = this.serializer;
        if (serializer != null) {
            return serializer.read(id, json);
        }
        return null;
    }
}
