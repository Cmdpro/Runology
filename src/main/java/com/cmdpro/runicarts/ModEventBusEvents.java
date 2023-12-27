package com.cmdpro.runicarts;

import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.api.RunicEnergyType;
import com.cmdpro.runicarts.config.RunicArtsConfig;
import com.cmdpro.runicarts.init.AttributeInit;
import com.cmdpro.runicarts.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = RunicArts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
    }
    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == RunicArtsConfig.COMMON_SPEC) {
            RunicArtsConfig.bake(config);
        }
    }
    @SubscribeEvent
    public static void registerStuff(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> {
            //helper.register(new ResourceLocation(RunicArts.MOD_ID, SoulShaperRecipe.Type.ID), SoulShaperRecipe.Type.INSTANCE);
        });
    }
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RunicArtsUtil.RUNIC_ENERGY_TYPES_REGISTRY = event.create(new RegistryBuilder<RunicEnergyType>()
                .setName(new ResourceLocation(RunicArts.MOD_ID, "runicenergytypes")));
    }
}
