package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.data.entries.*;
import com.cmdpro.runology.data.runechiseling.RuneChiselingResult;
import com.cmdpro.runology.data.runechiseling.RuneChiselingResultManager;
import com.cmdpro.runology.data.runechiseling.RuneChiselingResultSerializer;
import com.cmdpro.runology.networking.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

public record RuneChiselingSyncS2CPacket(Map<ResourceLocation, RuneChiselingResult> results) implements Message {
    public static RuneChiselingSyncS2CPacket read(FriendlyByteBuf buf) {
        Map<ResourceLocation, RuneChiselingResult> results = buf.readMap(ResourceLocation.STREAM_CODEC, (pBuffer) -> RuneChiselingResultSerializer.STREAM_CODEC.decode((RegistryFriendlyByteBuf)pBuffer));
        return new RuneChiselingSyncS2CPacket(results);
    }

    public static void write(RegistryFriendlyByteBuf buf, RuneChiselingSyncS2CPacket obj) {
        buf.writeMap(obj.results, ResourceLocation.STREAM_CODEC, (pBuffer, pValue) -> RuneChiselingResultSerializer.STREAM_CODEC.encode((RegistryFriendlyByteBuf)pBuffer, pValue));
    }
    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.handle(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<RuneChiselingSyncS2CPacket> TYPE = new Type<>(Runology.locate("rune_chiseling_sync"));

    private static class ClientHandler {
        public static void handle(RuneChiselingSyncS2CPacket packet) {
            RuneChiselingResultManager.types.clear();
            RuneChiselingResultManager.types.putAll(packet.results);
        }
    }
}