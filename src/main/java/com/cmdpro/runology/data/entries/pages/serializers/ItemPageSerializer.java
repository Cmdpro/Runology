package com.cmdpro.runology.data.entries.pages.serializers;

import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.ItemPage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class ItemPageSerializer extends PageSerializer<ItemPage> {
    public static final ItemPageSerializer INSTANCE = new ItemPageSerializer();
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPage> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.text);
        ItemStack.STREAM_CODEC.encode(pBuffer, pValue.item);
    }, (pBuffer) -> {
        Component text = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        ItemStack item = ItemStack.STREAM_CODEC.decode(pBuffer);
        return new ItemPage(text, item);
    });
    public static final MapCodec<ItemPage> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(page -> page.text),
            ItemStack.CODEC.fieldOf("item").forGetter(page -> page.item)
    ).apply(instance, ItemPage::new));
    @Override
    public MapCodec<ItemPage> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ItemPage> getStreamCodec() {
        return STREAM_CODEC;
    }
}
