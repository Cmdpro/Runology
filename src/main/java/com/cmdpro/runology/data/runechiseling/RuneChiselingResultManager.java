package com.cmdpro.runology.data.runechiseling;

import com.cmdpro.runology.Runology;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class RuneChiselingResultManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static RuneChiselingResultManager instance;
    protected RuneChiselingResultManager() {
        super(GSON, "runology/rune_chiseling");
    }
    public static RuneChiselingResultManager getOrCreateInstance() {
        if (instance == null) {
            instance = new RuneChiselingResultManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, RuneChiselingResult> types = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        types.clear();
        Runology.LOGGER.info("Adding Runology Rune Chiseling Results");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                RuneChiselingResult tab = deserializeRune(location, GsonHelper.convertToJsonObject(i.getValue(), "top member"));
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
    public static RuneChiselingResultSerializer serializer = new RuneChiselingResultSerializer();
    protected RuneChiselingResult deserializeRune(ResourceLocation id, JsonObject json) {
        RuneChiselingResultSerializer serializer = this.serializer;
        if (serializer != null) {
            return serializer.read(id, json);
        }
        return null;
    }
}
