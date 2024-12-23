package com.cmdpro.runology.rune;

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
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Optional;

public class RuneTypeSerializer {
    public RuneType read(ResourceLocation id, JsonObject json) {
        RuneType rune = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        rune.id = id;
        return rune;
    }
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneType> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(pValue.id);
        pBuffer.writeInt(pValue.color.getRed());
        pBuffer.writeInt(pValue.color.getGreen());
        pBuffer.writeInt(pValue.color.getBlue());
        pBuffer.writeBoolean(pValue.requiredAdvancement.isPresent());
        pValue.requiredAdvancement.ifPresent(pBuffer::writeResourceLocation);
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.name);
    }, (pBuffer) -> {
        ResourceLocation id = pBuffer.readResourceLocation();
        int r = pBuffer.readInt();
        int g = pBuffer.readInt();
        int b = pBuffer.readInt();
        Color color = new Color(r, g, b, 255);
        Optional<ResourceLocation> advancement = Optional.empty();
        if (pBuffer.readBoolean()) {
            ResourceLocation.STREAM_CODEC.decode(pBuffer);
        }
        Component name = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        RuneType entry = new RuneType(id, advancement, color, name);
        return entry;
    });
    public static final MapCodec<RuneType> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.STRING.fieldOf("color").forGetter((entry) -> "#" + Integer.toHexString(entry.color.getRGB()).substring(2)),
            ResourceLocation.CODEC.optionalFieldOf("advancement").forGetter((entry) -> entry.requiredAdvancement),
            ComponentSerialization.CODEC.fieldOf("name").forGetter((entry) -> entry.name)
    ).apply(instance, (color, advancement, name) -> new RuneType(null, advancement, Color.decode(color), name)));
}