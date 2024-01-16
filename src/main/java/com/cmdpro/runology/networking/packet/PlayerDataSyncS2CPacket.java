package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.moddata.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataSyncS2CPacket {
    private final int runicKnowledge;
    private final float currentChunkInstability;

    public PlayerDataSyncS2CPacket(int runicKnowledge, float currentChunkInstability) {
        this.runicKnowledge = runicKnowledge;
        this.currentChunkInstability = currentChunkInstability;
    }

    public PlayerDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.runicKnowledge = buf.readInt();
        this.currentChunkInstability = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(runicKnowledge);
        buf.writeFloat(currentChunkInstability);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(supplier));
        });
        context.setPacketHandled(true);
    }

    public void handlePacket(Supplier<NetworkEvent.Context> supplier) {
        ClientPlayerData.set(runicKnowledge, currentChunkInstability);
    }
}