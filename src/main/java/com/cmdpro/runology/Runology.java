package com.cmdpro.runology;

import com.cmdpro.runology.init.*;
import com.cmdpro.runology.integration.BookRunicRecipePage;
import com.cmdpro.runology.integration.BookRunicRecipePageRenderer;
import com.cmdpro.runology.integration.RunologyModonomiconConstants;
import com.cmdpro.runology.integration.bookconditions.BookRunicKnowledgeCondition;
import com.cmdpro.runology.networking.ModMessages;
import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;


import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("runology")
@Mod.EventBusSubscriber(modid = Runology.MOD_ID)
public class Runology
{
    public static ResourceKey<DamageType> soulExplosion = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Runology.MOD_ID, "soulexplosion"));

    public static final String MOD_ID = "runology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static RandomSource random;
    public static ImmutableList<EntityType<? extends LivingEntity>> soulCrystalEntities = ImmutableList.of();
    public Runology()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        EntityInit.ENTITY_TYPES.register(bus);
        MenuInit.register(bus);
        RecipeInit.register(bus);
        SoundInit.register(bus);
        CreativeModeTabInit.register(bus);
        ParticleInit.register(bus);
        AttributeInit.ATTRIBUTES.register(bus);
        RunicEnergyInit.RUNIC_ENERGY_TYPES.register(bus);
        GeckoLib.initialize();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addCreative);
        random = RandomSource.create();
        //modLoadingContext.registerConfig(ModConfig.Type.COMMON, RunologyConfig.COMMON_SPEC, "runology.toml");

        LoaderRegistry.registerConditionLoader(new ResourceLocation(MOD_ID, "knowledge"), BookRunicKnowledgeCondition::fromJson, BookRunicKnowledgeCondition::fromNetwork);
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {

        }
        if (event.getTabKey() == CreativeModeTabInit.ITEMS.getKey()) {
            event.accept(ItemInit.COPPERGAUNTLET);
            event.accept(ItemInit.BLANKRUNE);
            event.accept(ItemInit.EARTHRUNE);
            event.accept(ItemInit.AIRRUNE);
            event.accept(ItemInit.WATERRUNE);
            event.accept(ItemInit.FIRERUNE);
            event.accept(ItemInit.ENERGYRUNE);
            event.accept(ItemInit.ICERUNE);
            event.accept(ItemInit.VOIDRUNE);
            event.accept(ItemInit.PLANTRUNE);
            event.accept(ItemInit.EARTHPOWDER);
            event.accept(ItemInit.WATERPOWDER);
            event.accept(ItemInit.AIRPOWDER);
            event.accept(ItemInit.FIREPOWDER);
        }
        if (event.getTabKey() == CreativeModeTabInit.BLOCKS.getKey()) {
            event.accept(ItemInit.RUNICWORKBENCHITEM);
            event.accept(BlockInit.EARTHORE);
            event.accept(BlockInit.WATERORE);
            event.accept(BlockInit.AIRORE);
            event.accept(BlockInit.FIREORE);
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        ModMessages.register();
        event.enqueueWork(ModCriteriaTriggers::register);
        LoaderRegistry.registerPredicate(new ResourceLocation("runology:empty"), (getter, pos, state) -> !state.isSolid());
        LoaderRegistry.registerPageLoader(RunologyModonomiconConstants.Page.RUNICRECIPE, BookRunicRecipePage::fromJson, BookRunicRecipePage::fromNetwork);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("runology", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }


}
