package com.cmdpro.runology.api;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RunologyRegistries {
    public static ResourceKey<Registry<PageSerializer>> PAGE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Runology.locate("page_types"));
    public static Registry<PageSerializer> PAGE_TYPE_REGISTRY = new RegistryBuilder<>(PAGE_TYPE_REGISTRY_KEY).sync(true).create();
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(PAGE_TYPE_REGISTRY);
    }
}
