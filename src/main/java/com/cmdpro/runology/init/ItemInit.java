package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.*;
import com.cmdpro.runology.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Runology.MOD_ID);
    public static final RegistryObject<Item> LIQUIDSOULSBUCKET = register("liquidsoulsbucket", () -> new BucketItem(FluidInit.SOURCELIQUIDSOULS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TRANSMUTATIVESOLUTIONBUCKET = register("transmutativesolutionbucket", () -> new BucketItem(FluidInit.SOURCETRANSMUTATIVESOLUTION, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REALITYSLICER = register("realityslicer", () -> new RealitySlicer(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REALITYBREAKER = register("realitybreaker", () -> new PickaxeItem(ModTiers.REALITY, -3, -2.4F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANCIENTDRAGONSBLADE = register("ancientdragonsblade", () -> new AncientDragonsBlade(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LANTERNOFFLAMES = register("lanternofflames", () -> new LanternOfFlames(new Item.Properties().stacksTo(1).defaultDurability(1000).fireResistant()));
    public static final RegistryObject<Item> AMETHYSTSTAFF = register("amethyststaff", () -> new Staff(new Item.Properties().stacksTo(1), 1, 200));
    public static final RegistryObject<Item> SHATTERCRYSTALSTAFF = register("shattercrystalstaff", () -> new Staff(new Item.Properties().stacksTo(1), 2, 500));
    public static final RegistryObject<Item> COPPERGAUNTLET = register("coppergauntlet", () -> new Gauntlet(new Item.Properties().stacksTo(1), 1, 200));
    public static final RegistryObject<Item> MYSTERIUMGAUNTLET = register("mysteriumgauntlet", () -> new Gauntlet(new Item.Properties().stacksTo(1), 2, 500));
    public static final RegistryObject<Item> EMPOWEREDMYSTERIUMGAUNTLET = register("empoweredmysteriumgauntlet", () -> new EmpoweredGauntlet(new Item.Properties().stacksTo(1), ItemInit.MYSTERIUMGAUNTLET.get()));
    public static final RegistryObject<Item> RUNICWORKBENCHITEM = register("runicworkbench", () -> new RunicWorkbenchItem(BlockInit.RUNICWORKBENCH.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPELLTABLEITEM = register("spelltable", () -> new SpellTableItem(BlockInit.SPELLTABLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUNICCAULDRONITEM = register("runiccauldron", () -> new RunicCauldronItem(BlockInit.RUNICCAULDRON.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENDERTRANSPORTERITEM = register("endertransporter", () -> new EnderTransporterItem(BlockInit.ENDERTRANSPORTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> VOIDGLASSITEM = register("voidglass", () -> new VoidGlassItem(BlockInit.VOIDGLASS.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUNICANALYZERITEM = register("runicanalyzer", () -> new RunicAnalyzerItem(BlockInit.RUNICANALYZER.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLANKRUNE = register("blankrune", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISMATICSHARD = register("prismaticshard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EARTHRUNE = register("earthrune", () -> new EarthRune(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "earth")));
    public static final RegistryObject<Item> FIRERUNE = register("firerune", () -> new FireRune(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "fire")));
    public static final RegistryObject<Item> WATERRUNE = register("waterrune", () -> new WaterRune(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "water")));
    public static final RegistryObject<Item> AIRRUNE = register("airrune", () -> new AirRune(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "air")));
    public static final RegistryObject<Item> ICERUNE = register("icerune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "ice")));
    public static final RegistryObject<Item> ENERGYRUNE = register("energyrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "energy")));
    public static final RegistryObject<Item> PLANTRUNE = register("plantrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "plant")));
    public static final RegistryObject<Item> VOIDRUNE = register("voidrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "void")));
    public static final RegistryObject<Item> INSTABILITYRUNE = register("instabilityrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "instability")));
    public static final RegistryObject<Item> EARTHPOWDER = register("earthpowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIREPOWDER = register("firepowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATERPOWDER = register("waterpowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AIRPOWDER = register("airpowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INSTABILITYPOWDER = register("instabilitypowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INSTABILITYRESONATOR = register("instabilityreader", () -> new InstabilityResonator(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHATTERBERRIES = register("shatterberries", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> RAWMYSTERIUM = register("rawmysterium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MYSTERIUMINGOT = register("mysteriumingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MYSTERIUMNUGGET = register("mysteriumnugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RUNICWASTE = register("runicwaste", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RESEARCH = register("research", () -> new Research(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MYSTERIUMTOTEM = register("mysteriumtotem", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MYSTERIUMCORE = register("mysteriumcore", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHATTEREDSOUL = register("shatteredsoul", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURITYARROW = register("purityarrow", () -> new PurityArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> PURITYPOWDER = register("puritypowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURITYRUNE = register("purityrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(Runology.MOD_ID, "purity")));
    public static final RegistryObject<Item> PURIFIEDFLESH = register("purifiedflesh", () -> new Item(new Item.Properties().food(ModFoods.PURIFIEDFLESH)));
    public static final RegistryObject<Item> DIMENSIONALTHREATMUSICDISC = register("dimensionalthreatmusicdisc", () -> new RecordItem(6, SoundInit.RUNICOVERSEER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2020));
    public static final RegistryObject<Item> ENHANCEDBONEMEAL = register("enhancedbonemeal", () -> new EnhancedBoneMeal(new Item.Properties()));
    public static final RegistryObject<Item> ECHOGOGGLES = register("echogoggles", () -> new ArmorItem(ModArmorMaterials.ECHOGOGGLES, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> DRAGONIUMINGOT = register("dragoniumingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISMATICINGOT = register("prismaticingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENTDRAGONSBLADETORNRESEARCH = register("ancientdragonsbladetornresearch", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECHOGOGGLESTORNRESEARCH = register("echogogglestornresearch", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LANTERNOFFLAMESTORNRESEARCH = register("lanternofflamestornresearch", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISMATICBLASTER = register("prismaticblaster", () -> new PrismaticBlaster(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FIREUPGRADE = register("fireupgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "fire")));
    public static final RegistryObject<Item> WATERUPGRADE = register("waterupgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "water")));
    public static final RegistryObject<Item> AIRUPGRADE = register("airupgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "air")));
    public static final RegistryObject<Item> EARTHUPGRADE = register("earthupgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "earth")));
    public static final RegistryObject<Item> TRANSFORMATIONUPGRADE = register("transformationupgrade", () -> new UpgradeItem(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "transformation")));

    public static final RegistryObject<Item> SUMMONTOTEMSPELLCRYSTAL = register("summontotemspellcrystal", () -> new SpellCrystal(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "summontotem")));
    public static final RegistryObject<Item> FIREBALLSPELLCRYSTAL = register("fireballspellcrystal", () -> new SpellCrystal(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "fireball")));
    public static final RegistryObject<Item> ICESHARDSSPELLCRYSTAL = register("iceshardspellcrystal", () -> new SpellCrystal(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "iceshards")));
    public static final RegistryObject<Item> CONJURESPARKSPELLCRYSTAL = register("conjuresparkspellcrystal", () -> new SpellCrystal(new Item.Properties().stacksTo(1), new ResourceLocation(Runology.MOD_ID, "conjurespark")));

    public static final RegistryObject<Item> RUNICCONSTRUCTSPAWNEGG = register("runicconstructspawnegg", () -> new ForgeSpawnEggItem(EntityInit.RUNICCONSTRUCT, 0x000000, 0xaa00aa, new Item.Properties()));
    public static final RegistryObject<Item> RUNICSCOUTSPAWNEGG = register("runicscoutspawnegg", () -> new ForgeSpawnEggItem(EntityInit.RUNICSCOUT, 0x000000, 0xaa00aa, new Item.Properties()));
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
    public static final RegistryObject<Item> RUNOLOGYGUIDEICON =
            register("runologyguideicon", () -> new Item(new Item.Properties()));
}
