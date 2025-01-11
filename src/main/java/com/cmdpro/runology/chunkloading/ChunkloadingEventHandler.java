package com.cmdpro.runology.chunkloading;

import com.cmdpro.runology.Runology;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;

@EventBusSubscriber(modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ChunkloadingEventHandler {
    public static TicketController shatterController;
    @SubscribeEvent
    public static void registerTicketControllers(RegisterTicketControllersEvent event) {
        shatterController = new TicketController(Runology.locate("shatter_controller"));
        event.register(shatterController);
    }
}
