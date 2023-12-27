package com.cmdpro.runicarts;

import com.cmdpro.runicarts.commands.SpiritmanyCommands;
import com.cmdpro.runicarts.init.AttributeInit;
import com.mojang.blaze3d.shaders.EffectProgram;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicArts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeModEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        SpiritmanyCommands.register(event.getDispatcher());
    }
    @SubscribeEvent
    public static void onLootTablesLoaded(LootTableLoadEvent event) {

        if (event.getName().toString().equals("minecraft:chests/simple_dungeon")) {
            event.getTable().addPool(
                    LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(RunicArts.MOD_ID, "inject/simple_dungeon")).setWeight(1))
                            .build()
            );
        }
        if (event.getName().toString().startsWith("minecraft:chests/") && !event.getName().toString().startsWith("minecraft:chests/village") && !event.getName().toString().equals("minecraft:chests/spawn_bonus_chest") && !event.getName().toString().equals("minecraft:chests/jungle_temple_dispenser") && !event.getName().toString().equals("minecraft:chests/ancient_city_ice_box")) {
            event.getTable().addPool(
                    LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(RunicArts.MOD_ID, "inject/ancientpagestructures")).setWeight(1))
                            .build()
            );
        }
    }
}
