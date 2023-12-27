package com.cmdpro.runicarts;

import com.cmdpro.runicarts.init.*;
import com.cmdpro.runicarts.integration.BookAltarRecipePage;
import com.cmdpro.runicarts.integration.RunicArtsModonomiconConstants;
import com.cmdpro.runicarts.integration.bookconditions.BookAncientKnowledgeCondition;
import com.cmdpro.runicarts.integration.bookconditions.BookKnowledgeCondition;
import com.cmdpro.runicarts.networking.ModMessages;
import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;


import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("runicarts")
@Mod.EventBusSubscriber(modid = RunicArts.MOD_ID)
public class RunicArts
{
    public static ResourceKey<DamageType> soulExplosion = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(RunicArts.MOD_ID, "soulexplosion"));

    public static final String MOD_ID = "runicarts";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static RandomSource random;
    public static ImmutableList<EntityType<? extends LivingEntity>> soulCrystalEntities = ImmutableList.of();
    public RunicArts()
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
        SoulcasterEffectInit.SOULCASTER_EFFECTS.register(bus);
        GeckoLib.initialize();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addCreative);
        random = RandomSource.create();
        //modLoadingContext.registerConfig(ModConfig.Type.COMMON, RunicArtsConfig.COMMON_SPEC, "runicarts.toml");

        LoaderRegistry.registerConditionLoader(new ResourceLocation(MOD_ID, "knowledge"), BookKnowledgeCondition::fromJson, BookKnowledgeCondition::fromNetwork);
        LoaderRegistry.registerConditionLoader(new ResourceLocation(MOD_ID, "ancientknowledge"), BookAncientKnowledgeCondition::fromJson, BookAncientKnowledgeCondition::fromNetwork);
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {

        }
        if (event.getTabKey() == CreativeModeTabInit.ITEMS.getKey()) {
            event.accept(ItemInit.COPPERGAUNTLET);
        }
        if (event.getTabKey() == CreativeModeTabInit.BLOCKS.getKey()) {

        }
        setupSoulCrystalEntities();
        if (event.getTabKey() == CreativeModeTabInit.FULLCRYSTALS.getKey()) {
            for (EntityType<? extends LivingEntity> i : RunicArts.soulCrystalEntities) {
                ItemStack stack = new ItemStack(ItemInit.FULLSOULCRYSTAL.get());
                CompoundTag tag = stack.getOrCreateTag();
                tag.put("entitydata", new CompoundTag());
                tag.putString("entitytype", ForgeRegistries.ENTITY_TYPES.getKey(i).toString());
                event.accept(stack);
            }
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        ModMessages.register();
        event.enqueueWork(ModCriteriaTriggers::register);
        LoaderRegistry.registerPredicate(new ResourceLocation("runicarts:airorfire"), (getter, pos, state) -> state.isAir() || state.is(Blocks.SOUL_FIRE) || state.is(Blocks.FIRE));
        LoaderRegistry.registerPredicate(new ResourceLocation("runicarts:empty"), (getter, pos, state) -> !state.isSolid());
        LoaderRegistry.registerPageLoader(RunicArtsModonomiconConstants.Page.ALTAR_RECIPE, BookAltarRecipePage::fromJson, BookAltarRecipePage::fromNetwork);
    }
    private void complete(final FMLLoadCompleteEvent event)
    {
        setupSoulCrystalEntities();
    }
    public void setupSoulCrystalEntities() {
        if (soulCrystalEntities.isEmpty()) {
            soulCrystalEntities = ImmutableList.copyOf(
                    ForgeRegistries.ENTITY_TYPES.getValues().stream()
                            .filter(DefaultAttributes::hasSupplier)
                            .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                            .filter((i) -> !i.getTags().toList().contains(Tags.EntityTypes.BOSSES))
                            .filter((i) -> !i.getDescriptionId().equals("entity.minecraft.player"))
                            .filter((i) -> !i.getDescriptionId().equals("entity.minecraft.warden"))
                            .collect(Collectors.toList()));
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("runicarts", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
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
