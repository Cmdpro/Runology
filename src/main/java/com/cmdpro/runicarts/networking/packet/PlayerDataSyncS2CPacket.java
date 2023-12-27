package com.cmdpro.runicarts.networking.packet;

import com.cmdpro.runicarts.moddata.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataSyncS2CPacket {
    private final float souls;
    private final int knowledge;
    private final int ancientknowledge;
    private final boolean canDoubleJump;

    public PlayerDataSyncS2CPacket(float souls, int knowledge, int ancientknowledge, boolean canDoubleJump) {
        this.souls = souls;
        this.knowledge = knowledge;
        this.ancientknowledge = ancientknowledge;
        this.canDoubleJump = canDoubleJump;
    }

    public PlayerDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.souls = buf.readFloat();
        this.knowledge = buf.readInt();
        this.ancientknowledge = buf.readInt();
        this.canDoubleJump = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(souls);
        buf.writeInt(knowledge);
        buf.writeInt(ancientknowledge);
        buf.writeBoolean(canDoubleJump);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPlayerData.set(souls, knowledge, ancientknowledge, canDoubleJump);
        });
        return true;
    }
}