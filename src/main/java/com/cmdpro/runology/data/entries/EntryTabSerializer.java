package com.cmdpro.runology.data.entries;

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
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class EntryTabSerializer {
    public EntryTab read(ResourceLocation entryId, JsonObject json) {
        EntryTab entry = ICondition.getWithWithConditionsCodec(CODEC, JsonOps.INSTANCE, json).orElse(null);
        if (entry != null) {
            entry.id = entryId;
            return entry;
        } else {
            return null;
        }
    }
    public static final StreamCodec<RegistryFriendlyByteBuf, EntryTab> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        pBuffer.writeResourceLocation(pValue.id);
        ItemStack.STREAM_CODEC.encode(pBuffer, pValue.icon);
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.name);
        pBuffer.writeInt(pValue.priority);
        pBuffer.writeOptional(pValue.advancement, FriendlyByteBuf::writeResourceLocation);
    }, (pBuffer) -> {
        ResourceLocation id = pBuffer.readResourceLocation();
        ItemStack icon = ItemStack.STREAM_CODEC.decode(pBuffer);
        Component name = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        int priority = pBuffer.readInt();
        Optional<ResourceLocation> advancement = pBuffer.readOptional(FriendlyByteBuf::readResourceLocation);
        EntryTab entry = new EntryTab(id, icon, name, priority, advancement);
        return entry;
    });
    public static final MapCodec<EntryTab> ORIGINAL_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ItemStack.CODEC.fieldOf("icon").forGetter((entry) -> entry.icon),
            ComponentSerialization.CODEC.fieldOf("name").forGetter((entry) -> entry.name),
            Codec.INT.fieldOf("priority").forGetter((entry) -> entry.priority),
            ResourceLocation.CODEC.optionalFieldOf("advancement").forGetter((entry) -> entry.advancement)
    ).apply(instance, (icon, name, priority, advancement) -> new EntryTab(null, icon, name, priority, advancement)));
    public static final Codec<Optional<WithConditions<EntryTab>>> CODEC = ConditionalOps.createConditionalCodecWithConditions(ORIGINAL_CODEC.codec());
}