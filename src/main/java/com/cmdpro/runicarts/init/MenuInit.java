package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RunicArts.MOD_ID);
    public static final RegistryObject<MenuType<SoulShaperMenu>> SOULSHAPER_MENU = MENUS.register("soulshaper_menu", () -> new MenuType(SoulShaperMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final RegistryObject<MenuType<SoulcastersTableMenu>> SOULCASTERSTABLE_MENU = MENUS.register("soulcasterstable_menu", () -> new MenuType(SoulcastersTableMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final RegistryObject<MenuType<DivinationTableMenu>> DIVINATIONTABLE_MENU = registerMenuType(DivinationTableMenu::new, "divinationtable_menu");

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
