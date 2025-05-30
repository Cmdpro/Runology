package com.cmdpro.runology.data.entries;

import com.cmdpro.runology.api.RunologyRegistries;
import com.cmdpro.runology.api.guidebook.Page;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.MultiblockPage;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class EntrySerializer {
    public Entry read(ResourceLocation id, JsonObject json) {
        Entry rune = CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
        rune.id = id;
        return rune;
    }
    public static final Codec<Page> PAGE_CODEC = RunologyRegistries.PAGE_TYPE_REGISTRY.byNameCodec().dispatch(Page::getSerializer, PageSerializer::getCodec);
    public static final MapCodec<Entry> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("tab").forGetter((entry) -> entry.tab),
            ItemStack.CODEC.fieldOf("icon").forGetter((entry) -> entry.icon),
            Codec.DOUBLE.fieldOf("x").forGetter((entry) -> entry.pos.x),
            Codec.DOUBLE.fieldOf("y").forGetter((entry) -> entry.pos.y),
            Codec.DOUBLE.fieldOf("z").forGetter((entry) -> entry.pos.z),
            PAGE_CODEC.listOf().fieldOf("pages").forGetter((entry) -> entry.pages),
            ResourceLocation.CODEC.listOf().fieldOf("parents").forGetter((entry) -> entry.parents),
            ComponentSerialization.CODEC.fieldOf("name").forGetter((entry) -> entry.name),
            ResourceLocation.CODEC.optionalFieldOf("advancement").forGetter((entry) -> entry.advancement)
    ).apply(instance, (tab, icon, x, y, z, pages, parents, name, advancement) -> new Entry(null, tab, icon, x, y, z, pages, parents, name, advancement)));

    public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeBoolean(pValue.id != null);
        if (pValue.id != null) {
            pBuffer.writeResourceLocation(pValue.id);
        }
        pBuffer.writeResourceLocation(pValue.tab);
        ItemStack.STREAM_CODEC.encode(pBuffer, pValue.icon);
        pBuffer.writeVec3(pValue.pos);
        pBuffer.writeCollection(pValue.pages, (buffer, value) -> {
            buffer.writeResourceLocation(RunologyRegistries.PAGE_TYPE_REGISTRY.getKey(value.getSerializer()));
            value.getSerializer().getStreamCodec().encode(buffer, value);
        });
        pBuffer.writeCollection(pValue.parents, FriendlyByteBuf::writeResourceLocation);
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.name);
        pBuffer.writeOptional(pValue.advancement, FriendlyByteBuf::writeResourceLocation);
    }, (pBuffer) -> {
        boolean idExists = pBuffer.readBoolean();
        ResourceLocation id = null;
        if (idExists) {
            id = pBuffer.readResourceLocation();
        }
        ResourceLocation tab = pBuffer.readResourceLocation();
        ItemStack icon = ItemStack.STREAM_CODEC.decode(pBuffer);
        Vec3 pos = pBuffer.readVec3();
        List<Page> pages = pBuffer.readList((buffer) -> {
            ResourceLocation key = buffer.readResourceLocation();
            return (Page)RunologyRegistries.PAGE_TYPE_REGISTRY.get(key).getStreamCodec().decode(buffer);
        });
        List<ResourceLocation> parents = pBuffer.readList(FriendlyByteBuf::readResourceLocation);
        Component name = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        Optional<ResourceLocation> advancement = pBuffer.readOptional(FriendlyByteBuf::readResourceLocation);
        return new Entry(id, tab, icon, pos.x, pos.y, pos.z, pages, parents, name, advancement);
    });
}