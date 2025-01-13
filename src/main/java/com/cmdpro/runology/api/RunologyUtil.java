package com.cmdpro.runology.api;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerPowerModeSyncS2CPacket;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class RunologyUtil {
    public static void activatePowerMode(Player player) {
        if (!player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
            player.setData(AttachmentTypeRegistry.PLAYER_POWER_MODE, true);
            ModMessages.sendToPlayersTrackingEntityAndSelf(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), (ServerPlayer) player);
            player.sendSystemMessage(Component.translatable("misc.runology.power_activate").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
    public static void deactivatePowerMode(Player player) {
        if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
            player.setData(AttachmentTypeRegistry.PLAYER_POWER_MODE, false);
            ModMessages.sendToPlayersTrackingEntityAndSelf(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), (ServerPlayer) player);
        }
    }
    public static ShatteredFlowNetwork getShatteredFlowNetworkFromUUID(Level level, UUID uuid) {
        for (ShatteredFlowNetwork i : level.getData(AttachmentTypeRegistry.SHATTERED_FLOW_NETWORKS)) {
            if (i.uuid.equals(uuid)) {
                return i;
            }
        }
        return null;
    }
}
