package com.cmdpro.runology;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = Runology.MODID)
public class GameEvents {
    @SubscribeEvent
    public static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getAdvancement().id().equals(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter"))) {
            event.getEntity().sendSystemMessage(Component.translatable("modonomicon.runology.guidebook.discover").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
