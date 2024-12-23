package com.cmdpro.runology;

import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.RuneTypeSyncS2CPacket;
import com.cmdpro.runology.rune.RuneChiselingResultManager;
import com.cmdpro.runology.rune.RuneTypeManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = Runology.MODID)
public class GameEvents {
    @SubscribeEvent
    public static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getAdvancement().id().equals(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter"))) {
            event.getEntity().sendSystemMessage(Component.translatable("modonomicon.runology.guidebook.discover").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(RuneTypeManager.getOrCreateInstance());
        event.addListener(RuneChiselingResultManager.getOrCreateInstance());
    }
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                syncToPlayer(player);
            }
        } else {
            syncToPlayer(event.getPlayer());
        }
    }
    protected static void syncToPlayer(ServerPlayer player) {
        ModMessages.sendToPlayer(new RuneTypeSyncS2CPacket(RuneTypeManager.types), player);
    }
}
