package com.cmdpro.runology.data.runechiseling;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class RuneChiselingResultSerializer {
    public RuneChiselingResult read(ResourceLocation id, JsonObject json) {
        RuneChiselingResult rune = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        rune.id = id;
        return rune;
    }
    public static final MapCodec<RuneChiselingResult> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("input").forGetter((obj) -> BuiltInRegistries.BLOCK.getKey(obj.input)),
            ResourceLocation.CODEC.fieldOf("rune").forGetter((obj) -> obj.rune),
            ResourceLocation.CODEC.fieldOf("output").forGetter((obj) -> BuiltInRegistries.BLOCK.getKey(obj.output))
    ).apply(instance, (input, rune, output) -> new RuneChiselingResult(null, BuiltInRegistries.BLOCK.get(input), rune, BuiltInRegistries.BLOCK.get(output))));
}