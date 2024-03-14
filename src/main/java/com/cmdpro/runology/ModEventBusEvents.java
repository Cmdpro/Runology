package com.cmdpro.runology;

import com.cmdpro.runology.api.*;
import com.cmdpro.runology.config.RunologyConfig;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.RunicOverseer;
import com.cmdpro.runology.entity.RunicScout;
import com.cmdpro.runology.entity.Totem;
import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.recipe.RunicCauldronFluidRecipe;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Runology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityInit.RUNICCONSTRUCT.get(), RunicConstruct.setAttributes());
        event.put(EntityInit.RUNICSCOUT.get(), RunicScout.setAttributes());
        event.put(EntityInit.RUNICOVERSEER.get(), RunicOverseer.setAttributes());
        event.put(EntityInit.TOTEM.get(), Totem.setAttributes());
    }
    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == RunologyConfig.COMMON_SPEC) {
            RunologyConfig.bake(config);
        }
    }
    @SubscribeEvent
    public static void registerStuff(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> {
            helper.register(new ResourceLocation(Runology.MOD_ID, RunicCauldronItemRecipe.Type.ID), RunicCauldronItemRecipe.Type.INSTANCE);
            helper.register(new ResourceLocation(Runology.MOD_ID, RunicCauldronFluidRecipe.Type.ID), RunicCauldronFluidRecipe.Type.INSTANCE);
        });
    }
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RunologyUtil.RUNIC_ENERGY_TYPES_REGISTRY = event.create(new RegistryBuilder<RunicEnergyType>()
                .setName(new ResourceLocation(Runology.MOD_ID, "runicenergytypes")));
        RunologyUtil.INSTABILITY_EVENTS_REGISTRY = event.create(new RegistryBuilder<InstabilityEvent>()
                .setName(new ResourceLocation(Runology.MOD_ID, "instabilityevents")));
        RunologyUtil.ANALYZE_TASKS_REGISTRY = event.create(new RegistryBuilder<AnalyzeTaskSerializer>()
                .setName(new ResourceLocation(Runology.MOD_ID, "analyzetasks")));
        RunologyUtil.SPELL_REGISTRY = event.create(new RegistryBuilder<Spell>()
                .setName(new ResourceLocation(Runology.MOD_ID, "spells")));
    }
    @SubscribeEvent
    public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {

        event.register(EntityInit.RUNICSCOUT.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(EntityInit.RUNICCONSTRUCT.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
