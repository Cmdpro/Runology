package com.cmdpro.runology.data.entries.pages.serializers;

import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.TextPage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

public class TextPageSerializer extends PageSerializer<TextPage> {
    public static final TextPageSerializer INSTANCE = new TextPageSerializer();
    public static final StreamCodec<RegistryFriendlyByteBuf, TextPage> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.text);
    }, (pBuffer) -> {
        Component text = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        return new TextPage(text);
    });
    public static final MapCodec<TextPage> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ComponentSerialization.CODEC.fieldOf("text").forGetter(page -> page.text)
    ).apply(instance, TextPage::new));
    @Override
    public MapCodec<TextPage> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TextPage> getStreamCodec() {
        return STREAM_CODEC;
    }
}
