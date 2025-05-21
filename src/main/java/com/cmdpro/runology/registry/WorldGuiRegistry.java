package com.cmdpro.runology.registry;

import com.cmdpro.databank.Databank;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.TestWorldGuiType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WorldGuiRegistry {
    public static final DeferredRegister<WorldGuiType> WORLD_GUI_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Databank.MOD_ID, "world_gui_types"),
            Runology.MODID);
    public static final Supplier<WorldGuiType> TEST = register("test", TestWorldGuiType::new);
    private static <T extends WorldGuiType> Supplier<T> register(final String name, final Supplier<T> gui) {
        return WORLD_GUI_TYPES.register(name, gui);
    }
}
