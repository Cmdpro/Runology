package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            Runology.MOD_ID);

    public static RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("runology_items", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.COPPERGAUNTLET.get()))
                    .title(Component.translatable("creativemodetab.runologyitems")).build());
    public static RegistryObject<CreativeModeTab> BLOCKS = CREATIVE_MODE_TABS.register("runology_blocks", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.RUNICWORKBENCHITEM.get()))
                    .title(Component.translatable("creativemodetab.runologyblocks")).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
