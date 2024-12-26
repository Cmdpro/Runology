package com.cmdpro.runology;

import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerPowerModeSyncS2CPacket;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class RunologyUtil {
    public static void activatePowerMode(Player player) {
        if (!player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
            player.setData(AttachmentTypeRegistry.PLAYER_POWER_MODE, true);
            ModMessages.sendToPlayersTrackingEntityAndSelf(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), (ServerPlayer) player);
        }
    }
    public static  void deactivatePowerMode(Player player) {
        if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
            player.setData(AttachmentTypeRegistry.PLAYER_POWER_MODE, false);
            ModMessages.sendToPlayersTrackingEntityAndSelf(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), (ServerPlayer) player);
        }
    }
}
