package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.RuneItem;
import com.cmdpro.runicarts.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicArts.MOD_ID);
    public static final RegistryObject<Item> COPPERGAUNTLET = register("coppergauntlet", () -> new CopperGauntlet(new Item.Properties()));
    public static final RegistryObject<Item> RUNICWORKBENCHITEM = register("runicworkbench", () -> new RunicWorkbenchItem(BlockInit.RUNICWORKBENCH.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLANKRUNE = register("blankrune", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EARTHRUNE = register("earthrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "earth")));
    public static final RegistryObject<Item> FIRERUNE = register("firerune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "fire")));
    public static final RegistryObject<Item> WATERRUNE = register("waterrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "water")));
    public static final RegistryObject<Item> AIRRUNE = register("airrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "air")));
    public static final RegistryObject<Item> ICERUNE = register("icerune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "ice")));
    public static final RegistryObject<Item> ENERGYRUNE = register("energyrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "energy")));
    public static final RegistryObject<Item> PLANTRUNE = register("plantrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "plant")));
    public static final RegistryObject<Item> VOIDRUNE = register("voidrune", () -> new RuneItem(new Item.Properties(), new ResourceLocation(RunicArts.MOD_ID, "void")));
    public static final RegistryObject<Item> EARTHPOWDER = register("earthpowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIREPOWDER = register("firepowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATERPOWDER = register("waterpowder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AIRPOWDER = register("airpowder", () -> new Item(new Item.Properties()));
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
    public static final RegistryObject<Item> SPIRITMANCYGUIDEICON =
            ITEMS.register("runicartsguideicon", () -> new Item(new Item.Properties()));
}
