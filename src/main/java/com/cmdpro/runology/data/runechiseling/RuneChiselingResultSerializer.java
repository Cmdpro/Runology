package com.cmdpro.runology.data.runechiseling;

import com.cmdpro.runology.api.RunologyRegistries;
import com.cmdpro.runology.api.guidebook.Page;
import com.cmdpro.runology.data.entries.Entry;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

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

    public static final StreamCodec<RegistryFriendlyByteBuf, RuneChiselingResult> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(pValue.id);
        pBuffer.writeResourceKey(BuiltInRegistries.BLOCK.getResourceKey(pValue.input).orElseThrow());
        pBuffer.writeResourceLocation(pValue.rune);
        pBuffer.writeResourceKey(BuiltInRegistries.BLOCK.getResourceKey(pValue.output).orElseThrow());
    }, (pBuffer) -> {
        ResourceLocation id = pBuffer.readResourceLocation();
        ResourceKey<Block> input = pBuffer.readResourceKey(Registries.BLOCK);
        ResourceLocation rune = pBuffer.readResourceLocation();
        ResourceKey<Block> output = pBuffer.readResourceKey(Registries.BLOCK);
        return new RuneChiselingResult(id, BuiltInRegistries.BLOCK.get(input), rune, BuiltInRegistries.BLOCK.get(output));
    });
}