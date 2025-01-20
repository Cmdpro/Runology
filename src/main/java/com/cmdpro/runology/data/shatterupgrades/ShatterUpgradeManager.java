package com.cmdpro.runology.data.shatterupgrades;

import com.cmdpro.runology.Runology;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class ShatterUpgradeManager extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static ShatterUpgradeManager instance;
    protected ShatterUpgradeManager() {
        super(GSON, "runology/shatter_upgrades");
    }
    public static ShatterUpgradeManager getOrCreateInstance() {
        if (instance == null) {
            instance = new ShatterUpgradeManager();
        }
        return instance;
    }
    public static Map<ResourceLocation, ShatterUpgrade> types = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        types.clear();
        Runology.LOGGER.info("Adding Runology Shatter Upgrades");
        for (Map.Entry<ResourceLocation, JsonElement> i : pObject.entrySet()) {
            ResourceLocation location = i.getKey();
            if (location.getPath().startsWith("_")) {
                continue;
            }

            try {
                ShatterUpgrade tab = deserializeRune(location, GsonHelper.convertToJsonObject(i.getValue(), "top member"));
                if (tab == null) {
                    Runology.LOGGER.info("Skipping loading element {} as its serializer returned null", location);
                    continue;
                }
                types.put(i.getKey(), tab);
            } catch (IllegalArgumentException | JsonParseException e) {
                Runology.LOGGER.error("Parsing error loading shatter upgrade {}", location, e);
            }
        }
        Runology.LOGGER.info("Loaded {} shatter upgrades", types.size());
    }
    public static ShatterUpgradeSerializer serializer = new ShatterUpgradeSerializer();
    protected ShatterUpgrade deserializeRune(ResourceLocation id, JsonObject json) {
        ShatterUpgradeSerializer serializer = this.serializer;
        if (serializer != null) {
            return serializer.read(id, json);
        }
        return null;
    }
}
