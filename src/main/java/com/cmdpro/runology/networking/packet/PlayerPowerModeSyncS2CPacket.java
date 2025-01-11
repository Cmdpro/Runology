package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record PlayerPowerModeSyncS2CPacket(int id, boolean active) implements Message {
    @Override
    public void handleClient(Minecraft minecraft, Player player) {
        if (player.level().getEntity(id) instanceof Player target) {
            target.setData(AttachmentTypeRegistry.PLAYER_POWER_MODE, active);
        }
    }

    public static void write(RegistryFriendlyByteBuf pBuffer, PlayerPowerModeSyncS2CPacket obj) {
        pBuffer.writeInt(obj.id);
        pBuffer.writeBoolean(obj.active);
    }
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final CustomPacketPayload.Type<PlayerPowerModeSyncS2CPacket> TYPE = new CustomPacketPayload.Type<>(Runology.locate("player_power_mode_sync"));

    public static PlayerPowerModeSyncS2CPacket read(RegistryFriendlyByteBuf buf) {
        int id = buf.readInt();
        boolean active = buf.readBoolean();
        return new PlayerPowerModeSyncS2CPacket(id, active);
    }

}