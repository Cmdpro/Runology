package com.cmdpro.runology.data.shatterupgrades;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Optional;

public class ShatterUpgradeSerializer {
    public ShatterUpgrade read(ResourceLocation id, JsonObject json) {
        ShatterUpgrade rune = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        rune.id = id;
        return rune;
    }
    public static final MapCodec<ShatterUpgrade> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("multiblock").forGetter((entry) -> entry.multiblock),
            Codec.INT.fieldOf("power").forGetter((entry) -> entry.power),
            Codec.INT.fieldOf("stability").forGetter((entry) -> entry.stability)
    ).apply(instance, (multiblock, power, stability) -> new ShatterUpgrade(null, multiblock, power, stability)));
}