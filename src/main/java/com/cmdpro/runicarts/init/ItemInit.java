package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.item.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicArts.MOD_ID);
    public static final RegistryObject<Item> COPPERGAUNTLET = register("coppergauntlet", () -> new CopperGauntlet(new Item.Properties()));
    public static final RegistryObject<Item> DIVINATIONTABLE_ITEM = register("divinationtable", () -> new DivinationTableItem(BlockInit.DIVINATIONTABLE.get(), new Item.Properties()));
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
    public static final RegistryObject<Item> SPIRITMANCYGUIDEICON =
            ITEMS.register("runicartsguideicon", () -> new Item(new Item.Properties()));
}
