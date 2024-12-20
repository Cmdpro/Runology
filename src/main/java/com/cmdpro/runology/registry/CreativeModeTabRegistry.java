package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,
            Runology.MODID);

    public static Supplier<CreativeModeTab> RUNOLOGY = register("runology", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(BlockRegistry.SHATTER.get()))
                    .title(Component.translatable("creativemodetab.runology")).build());
    public static ResourceKey getKey(CreativeModeTab tab) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).get();
    }
    private static <T extends CreativeModeTab> Supplier<T> register(final String name, final Supplier<T> tab) {
        return CREATIVE_MODE_TABS.register(name, tab);
    }
}
