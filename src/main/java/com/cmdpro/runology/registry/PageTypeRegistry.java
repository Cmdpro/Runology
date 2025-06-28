package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PageTypeRegistry {
    public static final DeferredRegister<PageSerializer> PAGE_TYPES = DeferredRegister.create(Runology.locate("page_types"), Runology.MODID);

    public static final Supplier<CraftingPageSerializer> CRAFTING = register("crafting", () -> CraftingPageSerializer.INSTANCE);
    public static final Supplier<TextPageSerializer> TEXT = register("text", () -> TextPageSerializer.INSTANCE);
    public static final Supplier<ItemPageSerializer> ITEM = register("item", () -> ItemPageSerializer.INSTANCE);
    public static final Supplier<MultiblockPageSerializer> MULTIBLOCK = register("multiblock", () -> MultiblockPageSerializer.INSTANCE);
    public static final Supplier<ImagePageSerializer> IMAGE = register("image", () -> ImagePageSerializer.INSTANCE);
    private static <T extends PageSerializer> Supplier<T> register(final String name, final Supplier<T> item) {
        return PAGE_TYPES.register(name, item);
    }
}
