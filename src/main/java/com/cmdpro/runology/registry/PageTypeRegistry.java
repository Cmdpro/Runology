package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.CraftingPageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.ItemPageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.MultiblockPageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.TextPageSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PageTypeRegistry {
    public static final DeferredRegister<PageSerializer> PAGE_TYPES = DeferredRegister.create(Runology.locate("page_types"), Runology.MODID);

    public static final Supplier<CraftingPageSerializer> CRAFTINGPAGE = register("crafting", () -> CraftingPageSerializer.INSTANCE);
    public static final Supplier<TextPageSerializer> TEXTPAGE = register("text", () -> TextPageSerializer.INSTANCE);
    public static final Supplier<ItemPageSerializer> ITEMPAGE = register("item", () -> ItemPageSerializer.INSTANCE);
    public static final Supplier<MultiblockPageSerializer> MULTIBLOCK_PAGE = register("multiblock", () -> MultiblockPageSerializer.INSTANCE);
    private static <T extends PageSerializer> Supplier<T> register(final String name, final Supplier<T> item) {
        return PAGE_TYPES.register(name, item);
    }
}
