package com.cmdpro.runicarts.networking.packet;

import com.cmdpro.runicarts.moddata.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataSyncS2CPacket {
    private final int runicKnowledge;

    public PlayerDataSyncS2CPacket(int runicKnowledge) {
        this.runicKnowledge = runicKnowledge;
    }

    public PlayerDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.runicKnowledge = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(runicKnowledge);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPlayerData.set(runicKnowledge);
        });
        return true;
    }
}