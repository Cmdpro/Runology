package com.cmdpro.runology;

import com.cmdpro.runology.registry.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Runology.MODID)
public class Runology
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "runology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Runology(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        AttachmentTypeRegistry.ATTACHMENT_TYPES.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        FeatureRegistry.FEATURES.register(modEventBus);
        CriteriaTriggerRegistry.TRIGGERS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        RecipeRegistry.RECIPES.register(modEventBus);
        RecipeRegistry.RECIPE_TYPES.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey().equals(CreativeModeTabRegistry.getKey(CreativeModeTabRegistry.RUNOLOGY.get()))) {
            event.accept(ItemRegistry.GUIDEBOOK.get());
            event.accept(BlockRegistry.SHATTER.get());
            event.accept(BlockRegistry.SHATTERSTONE.get());
            event.accept(ItemRegistry.GOLD_CHISEL.get());
            event.accept(ItemRegistry.RUNIC_CHISEL.get());
            event.accept(BlockRegistry.SHATTERED_FOCUS.get());
            event.accept(BlockRegistry.SHATTERED_RELAY.get());
            event.accept(BlockRegistry.RUNE_HEAT_SHATTERSTONE.get());
            event.accept(BlockRegistry.RUNE_FROST_SHATTERSTONE.get());
            event.accept(BlockRegistry.RUNE_SHAPE_SHATTERSTONE.get());
            event.accept(BlockRegistry.RUNE_MOTION_SHATTERSTONE.get());
        }
    }
}
