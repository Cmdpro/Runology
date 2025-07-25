package com.cmdpro.runology.networking;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {
    public class Handler {
        public static <T extends CustomPacketPayload> void handle(T message, IPayloadContext ctx) {
            if (message instanceof Message msg) {
                if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
                    ctx.enqueueWork(() -> {
                        Server.handle(msg, ctx);
                    });
                } else {
                    ctx.enqueueWork(() -> {
                        Client.handle(msg, ctx);
                    });
                }
            }
        }
        public class Client {
            public static <T extends Message> void handle(T message, IPayloadContext ctx) {
                message.handleClient(Minecraft.getInstance(), Minecraft.getInstance().player, ctx);
            }
        }
        public class Server {
            public static <T extends Message> void handle(T message, IPayloadContext ctx) {
                message.handleServer(ctx.player().getServer(), (ServerPlayer)ctx.player(), ctx);
            }
        }
        public abstract interface Reader<T extends Message> {
            public abstract T read(RegistryFriendlyByteBuf buf);
        }
        public abstract interface Writer<T extends Message> {
            public abstract void write(RegistryFriendlyByteBuf buf, T message);
        }
    }
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Runology.MODID)
                .versioned("1.0");

        //S2C
        registrar.playToClient(RuneTypeSyncS2CPacket.TYPE, getNetworkCodec(RuneTypeSyncS2CPacket::read, RuneTypeSyncS2CPacket::write), Handler::handle);
        registrar.playToClient(StartFalseDeathS2CPacket.TYPE, getNetworkCodec(StartFalseDeathS2CPacket::read, StartFalseDeathS2CPacket::write), Handler::handle);
        registrar.playToClient(PlayerPowerModeSyncS2CPacket.TYPE, getNetworkCodec(PlayerPowerModeSyncS2CPacket::read, PlayerPowerModeSyncS2CPacket::write), Handler::handle);
        registrar.playToClient(EntrySyncS2CPacket.TYPE, getNetworkCodec(EntrySyncS2CPacket::read, EntrySyncS2CPacket::write), Handler::handle);
        registrar.playToClient(ShatterExplodeS2CPacket.TYPE, getNetworkCodec(ShatterExplodeS2CPacket::read, ShatterExplodeS2CPacket::write), Handler::handle);
        //C2S
        registrar.playToServer(BlinkC2SPacket.TYPE, getNetworkCodec(BlinkC2SPacket::read, BlinkC2SPacket::write), Handler::handle);
    }

    public static <T extends Message> StreamCodec<RegistryFriendlyByteBuf, T> getNetworkCodec(Handler.Reader<T> reader, Handler.Writer<T> writer) {
        return StreamCodec.of(writer::write, reader::read);
    }

    public static <T extends Message> void sendToServer(T message) {
        PacketDistributor.sendToServer(message);
    }

    public static <T extends Message> void sendToPlayer(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }
    public static <T extends Message> void sendToPlayersNear(T message, ServerLevel level, Vec3 position, float radius) {
        PacketDistributor.sendToPlayersNear(level, null, position.x, position.y, position.z, radius, message);
    }
    public static <T extends Message> void sendToPlayersTrackingEntityAndSelf(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, message);
    }
}
