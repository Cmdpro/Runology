package com.cmdpro.runology.networking;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }
    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Runology.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PlayerDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerDataSyncS2CPacket::new)
                .encoder(PlayerDataSyncS2CPacket::toBytes)
                .consumerMainThread(PlayerDataSyncS2CPacket::handle)
                .add();
        net.messageBuilder(PlayerUnlockEntryC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerUnlockEntryC2SPacket::new)
                .encoder(PlayerUnlockEntryC2SPacket::toBytes)
                .consumerMainThread(PlayerUnlockEntryC2SPacket::handle)
                .add();
        net.messageBuilder(PlayerClickAnalyzeButtonC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerClickAnalyzeButtonC2SPacket::new)
                .encoder(PlayerClickAnalyzeButtonC2SPacket::toBytes)
                .consumerMainThread(PlayerClickAnalyzeButtonC2SPacket::handle)
                .add();
        net.messageBuilder(DisplayEnderTransporterParticleLineS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DisplayEnderTransporterParticleLineS2CPacket::new)
                .encoder(DisplayEnderTransporterParticleLineS2CPacket::toBytes)
                .consumerMainThread(DisplayEnderTransporterParticleLineS2CPacket::handle)
                .add();
        net.messageBuilder(DisplayInstabilityGenerationS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DisplayInstabilityGenerationS2CPacket::new)
                .encoder(DisplayInstabilityGenerationS2CPacket::toBytes)
                .consumerMainThread(DisplayInstabilityGenerationS2CPacket::handle)
                .add();

    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
