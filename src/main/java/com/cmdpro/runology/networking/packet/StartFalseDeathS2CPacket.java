package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.rune.RuneType;
import com.cmdpro.runology.rune.RuneTypeManager;
import com.cmdpro.runology.rune.RuneTypeSerializer;
import com.cmdpro.runology.screen.FalseDeathScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public record StartFalseDeathS2CPacket(Component cause, boolean hardcore) implements Message {
    public static StartFalseDeathS2CPacket read(RegistryFriendlyByteBuf buf) {
        Component cause = ComponentSerialization.STREAM_CODEC.decode(buf);
        boolean hardcore = buf.readBoolean();
        return  new StartFalseDeathS2CPacket(cause, hardcore);
    }

    @Override
    public void handleClient(Minecraft minecraft, Player player) {
        minecraft.setScreen(new FalseDeathScreen(cause, hardcore));
    }

    public static void write(RegistryFriendlyByteBuf buf, StartFalseDeathS2CPacket obj) {
        ComponentSerialization.STREAM_CODEC.encode(buf, obj.cause);
        buf.writeBoolean(obj.hardcore);
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<StartFalseDeathS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "start_false_death"));
}