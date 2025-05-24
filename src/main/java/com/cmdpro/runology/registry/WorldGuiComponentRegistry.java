package com.cmdpro.runology.registry;

import com.cmdpro.databank.Databank;
import com.cmdpro.databank.worldgui.components.WorldGuiComponentType;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.worldgui.components.TestButtonComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WorldGuiComponentRegistry {
    public static final DeferredRegister<WorldGuiComponentType> WORLD_GUI_COMPONENTS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Databank.MOD_ID, "world_gui_components"),
            Runology.MODID);
    public static final Supplier<WorldGuiComponentType> TEST_BUTTON = register("test_button", TestButtonComponentType::new);
    private static <T extends WorldGuiComponentType> Supplier<T> register(final String name, final Supplier<T> gui) {
        return WORLD_GUI_COMPONENTS.register(name, gui);
    }
}
