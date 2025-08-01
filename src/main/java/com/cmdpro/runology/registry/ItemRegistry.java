package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Runology.MODID);
    public static final Supplier<Item> GUIDEBOOK = register("guidebook", () -> new RunicCodexItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> RUNIC_CHISEL = register("runic_chisel", () -> new RunicChisel(new Item.Properties().durability(25)));
    public static final Supplier<Item> GOLD_CHISEL = register("gold_chisel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> SHATTER_READER = register("shatter_reader", () -> new ShatterReader(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> SHATTERED_SHARD = register("shattered_shard", () -> new Item(new Item.Properties().fireResistant()));
    public static final Supplier<Item> BLINK_BOOTS = register("blink_boots", () -> new BlinkBoots(new Item.Properties().stacksTo(1).durability(ArmorItem.Type.BOOTS.getDurability(15))));
    public static final Supplier<Item> SHATTERED_INGOT = register("shattered_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SHATTER_EXPLARDER = register("shatter_explarder", () -> new ShatterExplarder(new Item.Properties()));

    public static final Supplier<Item> SHATTERED_FLOW_ICON = register("shattered_flow_icon", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BASIC_RUNES_ICON = register("basic_runes_icon", () -> new Item(new Item.Properties()));
    private static <T extends Item> Supplier<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
