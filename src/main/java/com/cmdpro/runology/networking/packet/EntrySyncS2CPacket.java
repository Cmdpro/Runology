package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.data.entries.*;
import com.cmdpro.runology.networking.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

public record EntrySyncS2CPacket(Map<ResourceLocation, Entry> entries, Map<ResourceLocation, EntryTab> tabs) implements Message {
    public static EntrySyncS2CPacket read(FriendlyByteBuf buf) {
        Map<ResourceLocation, Entry> entries = buf.readMap(ResourceLocation.STREAM_CODEC, (pBuffer) -> EntrySerializer.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        Map<ResourceLocation, EntryTab> tabs = buf.readMap(ResourceLocation.STREAM_CODEC, (pBuffer) -> EntryTabSerializer.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        return new EntrySyncS2CPacket(entries, tabs);
    }

    public static void write(RegistryFriendlyByteBuf buf, EntrySyncS2CPacket obj) {
        buf.writeMap(obj.entries, ResourceLocation.STREAM_CODEC, (pBuffer, pValue) -> EntrySerializer.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
        buf.writeMap(obj.tabs, ResourceLocation.STREAM_CODEC, (pBuffer, pValue) -> EntryTabSerializer.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
    }
    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.handle(this);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final CustomPacketPayload.Type<EntrySyncS2CPacket> TYPE = new CustomPacketPayload.Type<>(Runology.locate("entry_sync"));

    private static class ClientHandler {
        public static void handle(EntrySyncS2CPacket packet) {
            EntryTabManager.tabs.clear();
            EntryTabManager.tabs.putAll(packet.tabs);
            EntryManager.entries.clear();
            EntryManager.entries.putAll(packet.entries);
            for (Entry i : EntryManager.entries.values()) {
                i.updateParentEntries();
            }
        }
    }
}