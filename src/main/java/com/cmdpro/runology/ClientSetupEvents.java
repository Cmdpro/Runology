package com.cmdpro.runology;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import com.cmdpro.runology.shaders.ShatterShader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = Runology.MODID)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event)
    {
        shatterShader = new ShatterShader();
        PostShaderManager.addShader(shatterShader);
        shatterShader.setActive(true);
    }
    public static PostShaderInstance shatterShader;
}
