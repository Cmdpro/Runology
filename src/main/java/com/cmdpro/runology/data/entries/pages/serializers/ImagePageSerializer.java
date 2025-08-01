package com.cmdpro.runology.data.entries.pages.serializers;

import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.ImagePage;
import com.cmdpro.runology.data.entries.pages.ItemPage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ImagePageSerializer extends PageSerializer<ImagePage> {
    public static final ImagePageSerializer INSTANCE = new ImagePageSerializer();
    public static final StreamCodec<RegistryFriendlyByteBuf, ImagePage> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {
        ComponentSerialization.STREAM_CODEC.encode(pBuffer, pValue.text);
        ResourceLocation.STREAM_CODEC.encode(pBuffer, pValue.image);
        pBuffer.writeFloat(pValue.scale);
        pBuffer.writeInt(pValue.verticalOffset);
    }, (pBuffer) -> {
        Component text = ComponentSerialization.STREAM_CODEC.decode(pBuffer);
        ResourceLocation image = ResourceLocation.STREAM_CODEC.decode(pBuffer);
        float scale = pBuffer.readFloat();
        int verticalOffset = pBuffer.readInt();
        return new ImagePage(text, image, scale, verticalOffset);
    });
    public static final MapCodec<ImagePage> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(page -> page.text),
            ResourceLocation.CODEC.fieldOf("image").forGetter(page -> page.image),
            Codec.FLOAT.optionalFieldOf("scale", 1f).forGetter(page -> page.scale),
            Codec.INT.optionalFieldOf("vertical_offset", 0).forGetter(page -> page.verticalOffset)
    ).apply(instance, ImagePage::new));
    @Override
    public MapCodec<ImagePage> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ImagePage> getStreamCodec() {
        return STREAM_CODEC;
    }
}
