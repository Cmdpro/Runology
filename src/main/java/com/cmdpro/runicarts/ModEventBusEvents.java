package com.cmdpro.runicarts;

import com.cmdpro.runicarts.api.SoulcasterEffect;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.config.RunicArtsConfig;
import com.cmdpro.runicarts.entity.*;
import com.cmdpro.runicarts.init.AttributeInit;
import com.cmdpro.runicarts.init.EntityInit;
import com.cmdpro.runicarts.recipe.SoulShaperRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.data.event.GatherDataEvent;
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
        event.put(EntityInit.SOULKEEPER.get(), SoulKeeper.setAttributes());
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
            helper.register(new ResourceLocation(RunicArts.MOD_ID, SoulShaperRecipe.Type.ID), SoulShaperRecipe.Type.INSTANCE);
        });
    }
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY = event.create(new RegistryBuilder<SoulcasterEffect>()
                .setName(new ResourceLocation(RunicArts.MOD_ID, "soulcaster_effects")));
    }
    @SubscribeEvent
    public static void attributeModifierEvent(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, AttributeInit.MAXSOULS.get());
    }
}
