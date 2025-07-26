package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.screen.FalseDeathScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StartFalseDeathS2CPacket(Component cause, boolean hardcore) implements Message {
    public static StartFalseDeathS2CPacket read(RegistryFriendlyByteBuf buf) {
        Component cause = ComponentSerialization.STREAM_CODEC.decode(buf);
        boolean hardcore = buf.readBoolean();
        return  new StartFalseDeathS2CPacket(cause, hardcore);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientPacketHandler.openScreen(minecraft, player, this);
    }

    public static void write(RegistryFriendlyByteBuf buf, StartFalseDeathS2CPacket obj) {
        ComponentSerialization.STREAM_CODEC.encode(buf, obj.cause);
        buf.writeBoolean(obj.hardcore);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<StartFalseDeathS2CPacket> TYPE = new Type<>(Runology.locate("start_false_death"));
    public static class ClientPacketHandler {
        public static void openScreen(Minecraft minecraft, Player player, StartFalseDeathS2CPacket packet) {
            minecraft.setScreen(new FalseDeathScreen(packet.cause, packet.hardcore));
        }
    }
}