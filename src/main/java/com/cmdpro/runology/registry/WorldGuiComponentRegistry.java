package com.cmdpro.runology.registry;

import com.cmdpro.databank.Databank;
import com.cmdpro.databank.worldgui.components.WorldGuiComponentType;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.worldgui.components.ExitPageComponentType;
import com.cmdpro.runology.worldgui.components.MultiblockViewComponentType;
import com.cmdpro.runology.worldgui.components.PageChangeComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WorldGuiComponentRegistry {
    public static final DeferredRegister<WorldGuiComponentType> WORLD_GUI_COMPONENTS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Databank.MOD_ID, "world_gui_components"),
            Runology.MODID);
    public static final Supplier<WorldGuiComponentType> MULTIBLOCK_VIEW = register("multiblock_view", MultiblockViewComponentType::new);
    public static final Supplier<PageChangeComponentType> PAGE_CHANGE = register("page_change", PageChangeComponentType::new);
    public static final Supplier<ExitPageComponentType> EXIT_PAGE = register("exit_page", ExitPageComponentType::new);
    private static <T extends WorldGuiComponentType> Supplier<T> register(final String name, final Supplier<T> gui) {
        return WORLD_GUI_COMPONENTS.register(name, gui);
    }
}
