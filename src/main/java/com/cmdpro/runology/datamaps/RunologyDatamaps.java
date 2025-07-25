package com.cmdpro.runology.datamaps;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RunologyDatamaps {
    public static final DataMapType<Block, ShatterConversionMap> SHATTER_CONVERSION = DataMapType.builder(
            Runology.locate("shatter_conversion"),
            Registries.BLOCK,
            ShatterConversionMap.CODEC
    ).synced(ShatterConversionMap.CODEC, true).build();
    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(SHATTER_CONVERSION);
    }
}
