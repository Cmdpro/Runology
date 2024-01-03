package com.cmdpro.runology;

import com.cmdpro.runology.commands.RunologyCommands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Runology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeModEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        RunologyCommands.register(event.getDispatcher());
    }
}
