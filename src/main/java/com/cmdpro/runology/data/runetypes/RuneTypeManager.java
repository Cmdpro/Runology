package com.cmdpro.runology.data.runetypes;

import com.cmdpro.runology.Runology;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class RuneTypeManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static RuneTypeManager instance;
    protected RuneTypeManager() {
        super(GSON, "runology/runes");
    }
    public static RuneTypeManager getOrCreateInstance() {
        if (instance == null) {
            instance = new RuneTypeManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, RuneType> types = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        types.clear();
        Runology.LOGGER.info("Adding Runology Rune Types");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                RuneType tab = deserializeRune(location, GsonHelper.convertToJsonObject(i.getValue(), "top member"));
                if (tab == null) {
                    Runology.LOGGER.info("Skipping loading element {} as its serializer returned null", location);
                    continue;
                }
                types.put(i.getKey(), tab);
            } catch (IllegalArgumentException | JsonParseException e) {
                Runology.LOGGER.error("Parsing error loading rune type {}", location, e);
            }
        }
        Runology.LOGGER.info("Loaded {} rune types", types.size());
    }
    public static RuneTypeSerializer serializer = new RuneTypeSerializer();
    protected RuneType deserializeRune(ResourceLocation id, JsonObject json) {
        RuneTypeSerializer serializer = this.serializer;
        if (serializer != null) {
            return serializer.read(id, json);
        }
        return null;
    }
}
