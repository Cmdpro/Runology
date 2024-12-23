package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.rune.RuneType;
import com.cmdpro.runology.rune.RuneTypeManager;
import com.cmdpro.runology.rune.RuneTypeSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public record RuneTypeSyncS2CPacket(Map<ResourceLocation, RuneType> entries) implements Message {
    public static RuneTypeSyncS2CPacket read(RegistryFriendlyByteBuf buf) {
        Map<ResourceLocation, RuneType> runes = buf.readMap(FriendlyByteBuf::readResourceLocation, (pBuffer) -> RuneTypeSerializer.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        return  new RuneTypeSyncS2CPacket(runes);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player) {
        RuneTypeManager.types.clear();
        RuneTypeManager.types.putAll(entries);
    }

    public static void write(RegistryFriendlyByteBuf buf, RuneTypeSyncS2CPacket obj) {
        buf.writeMap(obj.entries, ResourceLocation.STREAM_CODEC, (pBuffer, pValue) -> RuneTypeSerializer.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<RuneTypeSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "rune_type_sync"));
}