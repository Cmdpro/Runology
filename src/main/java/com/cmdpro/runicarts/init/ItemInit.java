package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.SoulGem;
import com.cmdpro.runicarts.api.Wand;
import com.cmdpro.runicarts.item.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicArts.MOD_ID);
    public static final RegistryObject<Item> COPPERGAUNTLET = register("coppergauntlet", () -> new CopperGauntlet(new Item.Properties()));




    public static final RegistryObject<Item> SOULMETALDAGGER = register("soulmetaldagger", () -> new SwordItem(ModTiers.SCYTHE, 1, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYDAGGER = register("purgatorydagger", () -> new SwordItem(ModTiers.PURGATORY, 1, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYSWORD = register("purgatorysword", () -> new PurgatorySword(ModTiers.PURGATORY, 5, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> SOULMETAL = register("soulmetal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIRITTANK_ITEM = register("spirittank", () -> new SpiritTankItem(BlockInit.SPIRITTANK.get(), new Item.Properties()));
    public static final RegistryObject<Item> DIVINATIONTABLE_ITEM = register("divinationtable", () -> new DivinationTableItem(BlockInit.DIVINATIONTABLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOULPOINT_ITEM = register("soulpoint", () -> new SoulPointItem(BlockInit.SOULPOINT.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOULALTAR_ITEM = register("soulaltar", () -> new SoulAltarItem(BlockInit.SOULALTAR.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOULLINKER = register("soullinker", () -> new SoulLinker(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SOULCRYSTAL = register("soulcrystal", () -> new SoulCrystal(new Item.Properties()));
    public static final RegistryObject<Item> SOULFOCUS = register("soulfocus", () -> new SoulFocus(new Item.Properties()));
    public static final RegistryObject<Item> ICECRYSTAL = register("icecrystal", () -> new IceCrystal(new Item.Properties()));
    public static final RegistryObject<Item> ENDERCRYSTAL = register("endercrystal", () -> new EnderCrystal(new Item.Properties()));
    public static final RegistryObject<Item> FLAMECRYSTAL = register("flamecrystal", () -> new FlameCrystal(new Item.Properties()));
    public static final RegistryObject<Item> LIFECRYSTAL = register("lifecrystal", () -> new LifeCrystal(new Item.Properties()));
    public static final RegistryObject<Item> DEATHCRYSTAL = register("deathcrystal", () -> new DeathCrystal(new Item.Properties()));
    public static final RegistryObject<Item> SOULMETALWAND = register("soulmetalwand", () -> new SoulmetalWand(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRYSTALSOULSMUSICDISC = register("crystalsoulsmusicdisc", () -> new RecordItem(6, SoundInit.CRYSTALSOULS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2220));
    public static final RegistryObject<Item> THESOULSSCREAMMUSICDISC = register("thesoulsscreammusicdisc", () -> new RecordItem(6, SoundInit.SOULKEEPERPHASE1, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 1860));
    public static final RegistryObject<Item> THESOULSREVENGEMUSICDISC = register("thesoulsrevengemusicdisc", () -> new RecordItem(6, SoundInit.SOULKEEPERPHASE2, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 1500));
    public static final RegistryObject<Item> FULLSOULCRYSTAL = register("fullsoulcrystal", () -> new FullSoulCrystal(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENTPAGE = register("ancientpage", () -> new AncientPage(new Item.Properties()));
    public static final RegistryObject<Item> SOULBARRIER = register("soulbarrier", () -> new SoulBarrier(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SOULORB = register("soulorb", () -> new SoulOrb(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SOULTRANSFORMER = register("soultransformer", () -> new SoulTransformer(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SOULBOOSTER =register("soulbooster", () -> new SoulBooster(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LAPISWAND = register("lapiswand", () -> new LapisWand(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AMETHYSTWAND = register("amethystwand", () -> new Wand(new Item.Properties().stacksTo(1), 0.9f));
    public static final RegistryObject<Item> ECHOWAND = register("echowand", () -> new Wand(new Item.Properties().stacksTo(1), 0.8f));
    public static final RegistryObject<Item> PURGATORYWAND = register("purgatorywand", () -> new Wand(new Item.Properties().stacksTo(1), 0.5f));
    public static final RegistryObject<Item> PURGATORYINGOT  = register("purgatoryingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYHELMET = register("purgatoryhelmet", () -> new PurgatoryArmor(ModArmorMaterials.PURGATORY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYCHESTPLATE = register("purgatorychestplate", () -> new PurgatoryArmor(ModArmorMaterials.PURGATORY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYLEGGINGS = register("purgatoryleggings", () -> new PurgatoryArmor(ModArmorMaterials.PURGATORY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> PURGATORYBOOTS = register("purgatoryboots", () -> new PurgatoryArmor(ModArmorMaterials.PURGATORY, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> EMPTYSOULGEM  = register("emptysoulgem", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> EASYSOULGEM  = register("easysoulgem", () -> new SoulGem(new Item.Properties(), 0, 0, 10));
    public static final RegistryObject<Item> MEDIUMSOULGEM  = register("mediumsoulgem", () -> new SoulGem(new Item.Properties(), 1, 1, 20));
    public static final RegistryObject<Item> HARDSOULGEM  = register("hardsoulgem", () -> new SoulGem(new Item.Properties(), 1, 2, 30));
    public static final RegistryObject<Item> INSANESOULGEM  = register("insanesoulgem", () -> new SoulGem(new Item.Properties(), 2, 1, 40));
    public static final RegistryObject<Item> STUDYRESULTS  = register("studyresults", () -> new StudyResults(new Item.Properties()));
	
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
    public static final RegistryObject<Item> SPIRITMANCYGUIDEICON =
            ITEMS.register("runicartsguideicon", () -> new Item(new Item.Properties()));
}
