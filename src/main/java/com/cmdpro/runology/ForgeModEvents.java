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
    @SubscribeEvent
    public static void onLootTablesLoaded(LootTableLoadEvent event) {

        if (event.getName().toString().equals("minecraft:chests/end_city_treasure")) {
            event.getTable().addPool(
                    LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Runology.MOD_ID, "inject/end_city")).setWeight(1))
                            .build()
            );
        }
        if (event.getName().toString().equals("minecraft:chests/ancient_city")) {
            event.getTable().addPool(
                    LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Runology.MOD_ID, "inject/ancient_city")).setWeight(1))
                            .build()
            );
        }
        if (event.getName().toString().equals("minecraft:chests/nether_bridge")) {
            event.getTable().addPool(
                    LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(Runology.MOD_ID, "inject/nether_fortress")).setWeight(1))
                            .build()
            );
        }
    }
}
