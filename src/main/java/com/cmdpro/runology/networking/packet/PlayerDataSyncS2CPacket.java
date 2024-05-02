package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.moddata.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PlayerDataSyncS2CPacket {
    private final float currentChunkInstability;

    public PlayerDataSyncS2CPacket(float currentChunkInstability) {
        this.currentChunkInstability = currentChunkInstability;
    }

    public PlayerDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.currentChunkInstability = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(currentChunkInstability);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePacket(this, supplier));
        });
        context.setPacketHandled(true);
    }

    public static class ClientPacketHandler {
        public static void handlePacket(PlayerDataSyncS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ClientPlayerData.set(msg.currentChunkInstability);
        }
    }
}