package com.cmdpro.runology;

import com.cmdpro.runology.config.RunologyConfig;
import com.cmdpro.runology.init.*;
import com.cmdpro.runology.integration.modonomicon.BookRunicCauldronFluidRecipePage;
import com.cmdpro.runology.integration.modonomicon.BookRunicCauldronItemRecipePage;
import com.cmdpro.runology.integration.modonomicon.BookRunicRecipePage;
import com.cmdpro.runology.integration.modonomicon.RunologyModonomiconConstants;
import com.cmdpro.runology.integration.modonomicon.bookconditions.BookAnalyzeTaskCondition;
import com.cmdpro.runology.networking.ModMessages;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
    public static final ResourceKey<DamageType> magicProjectile = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Runology.MOD_ID, "magicprojectile"));
    public static final String MOD_ID = "runology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static RandomSource random;
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
        InstabilityEventInit.INSTABILITY_EVENTS.register(bus);
        AnalyzeTaskInit.ANALYZE_TASKS.register(bus);
        SpellInit.SPELLS.register(bus);
        FluidInit.register(bus);
        FluidTypeInit.FLUID_TYPES.register(bus);
        StructureInit.register(bus);
        FeatureInit.register(bus);
        GeckoLib.initialize();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addCreative);
        random = RandomSource.create();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, RunologyConfig.COMMON_SPEC, "runology.toml");

        LoaderRegistry.registerConditionLoader(new ResourceLocation(MOD_ID, "analyze"), BookAnalyzeTaskCondition::fromJson, BookAnalyzeTaskCondition::fromNetwork);
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemInit.RUNICCONSTRUCTSPAWNEGG);
            event.accept(ItemInit.RUNICSCOUTSPAWNEGG);
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
            event.accept(ItemInit.SHATTERBERRIES);
            event.accept(ItemInit.RAWMYSTERIUM);
            event.accept(ItemInit.MYSTERIUMINGOT);
            event.accept(ItemInit.MYSTERIUMNUGGET);
            event.accept(ItemInit.MYSTERIUMTOTEM);
            event.accept(ItemInit.INSTABILITYRESONATOR);
            event.accept(ItemInit.MYSTERIUMCORE);
            event.accept(ItemInit.MYSTERIUMGAUNTLET);
            event.accept(ItemInit.EMPOWEREDMYSTERIUMGAUNTLET);
            event.accept(ItemInit.REALITYSLICER);
            event.accept(ItemInit.SHATTEREDSOUL);
            event.accept(ItemInit.PURITYARROW);
            event.accept(ItemInit.INSTABILITYPOWDER);
            event.accept(ItemInit.INSTABILITYRUNE);
            event.accept(ItemInit.PURITYPOWDER);
            event.accept(ItemInit.PURITYRUNE);
            event.accept(ItemInit.AMETHYSTSTAFF);
            event.accept(ItemInit.SHATTERCRYSTALSTAFF);
            event.accept(ItemInit.SUMMONTOTEMSPELLCRYSTAL);
            event.accept(ItemInit.FIREBALLSPELLCRYSTAL);
            event.accept(ItemInit.ICESHARDSSPELLCRYSTAL);
            event.accept(ItemInit.PURIFIEDFLESH);
            event.accept(ItemInit.ENHANCEDBONEMEAL);
            event.accept(ItemInit.ECHOGOGGLES);
            event.accept(ItemInit.DRAGONIUMINGOT);
            event.accept(ItemInit.ANCIENTDRAGONSBLADE);
            ItemStack lanternofflames = new ItemStack(ItemInit.LANTERNOFFLAMES.get());
            lanternofflames.setDamageValue(lanternofflames.getMaxDamage()-1);
            event.accept(lanternofflames);
            event.accept(ItemInit.LANTERNOFFLAMES);
            event.accept(ItemInit.ANCIENTDRAGONSBLADETORNRESEARCH);
            event.accept(ItemInit.ECHOGOGGLESTORNRESEARCH);
            event.accept(ItemInit.LANTERNOFFLAMESTORNRESEARCH);
            event.accept(ItemInit.RUNICWASTE);
            event.accept(ItemInit.LIQUIDSOULSBUCKET);
            event.accept(ItemInit.TRANSMUTATIVESOLUTIONBUCKET);
            event.accept(ItemInit.CONJURESPARKSPELLCRYSTAL);
            event.accept(ItemInit.DIMENSIONALTHREATMUSICDISC);
        }
        if (event.getTabKey() == CreativeModeTabInit.BLOCKS.getKey()) {
            event.accept(ItemInit.RUNICWORKBENCHITEM);
            event.accept(BlockInit.EARTHORE);
            event.accept(BlockInit.WATERORE);
            event.accept(BlockInit.AIRORE);
            event.accept(BlockInit.FIREORE);
            event.accept(BlockInit.SHATTERSTONE);
            event.accept(BlockInit.SHATTERSTONEBRICKS);
            event.accept(BlockInit.SHATTERSTONEBRICKSLAB);
            event.accept(BlockInit.SHATTERSTONEPILLAR);
            event.accept(BlockInit.SHATTERSTONEBRICKSTAIRS);
            event.accept(BlockInit.SHATTERSTONEBRICKWALL);
            event.accept(BlockInit.SHATTERSTONESLAB);
            event.accept(BlockInit.SHATTERSTONESTAIRS);
            event.accept(BlockInit.SHATTERSTONEWALL);
            event.accept(BlockInit.CHISELEDSHATTERSTONEBRICKS);
            event.accept(BlockInit.CRACKEDSHATTERSTONEBRICKS);
            event.accept(BlockInit.SHATTERLEAF);
            event.accept(BlockInit.MYSTERIUMORE);
            event.accept(BlockInit.MYSTERIUMBLOCK);
            event.accept(BlockInit.RAWMYSTERIUMBLOCK);
            event.accept(BlockInit.SHATTERCRYSTAL);
            event.accept(BlockInit.PETRIFIEDSHATTERWOOD);
            event.accept(ItemInit.VOIDGLASSITEM);
            event.accept(BlockInit.MYSTERIOUSALTAR);
            event.accept(ItemInit.RUNICANALYZERITEM);
            event.accept(ItemInit.SPELLTABLEITEM);
            event.accept(ItemInit.RUNICCAULDRONITEM);
            event.accept(ItemInit.ENDERTRANSPORTERITEM);
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        ModMessages.register();
        event.enqueueWork(ModCriteriaTriggers::register);
        LoaderRegistry.registerPredicate(new ResourceLocation(Runology.MOD_ID, "empty"), (getter, pos, state) -> !state.isSolid());
        LoaderRegistry.registerPageLoader(RunologyModonomiconConstants.Page.RUNICRECIPE, BookRunicRecipePage::fromJson, BookRunicRecipePage::fromNetwork);
        LoaderRegistry.registerPageLoader(RunologyModonomiconConstants.Page.RUNICCAULDRONITEM, BookRunicCauldronItemRecipePage::fromJson, BookRunicCauldronItemRecipePage::fromNetwork);
        LoaderRegistry.registerPageLoader(RunologyModonomiconConstants.Page.RUNICCAULDRONFLUID, BookRunicCauldronFluidRecipePage::fromJson, BookRunicCauldronFluidRecipePage::fromNetwork);
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.SHATTERLEAF.getId(), BlockInit.POTTED_SHATTERLEAF);
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        //InterModComms.sendTo("runology", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {

        //Some example code to receive and process InterModComms from other mods
        /*
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
        */
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

        // Do something when the server starts
    }


}
