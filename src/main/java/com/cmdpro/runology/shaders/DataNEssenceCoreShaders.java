package com.cmdpro.runology.shaders;

import com.cmdpro.runology.Runology;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataNEssenceCoreShaders {
    public static ShaderInstance SHATTER;
    public static ShaderInstance getShatter() {
        return SHATTER;
    }
    public static ShaderInstance SHATTER_OUTLINE;
    public static ShaderInstance getShatterOutline() {
        return SHATTER_OUTLINE;
    }
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter"), DefaultVertexFormat.PARTICLE), shader -> { SHATTER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter_outline"), DefaultVertexFormat.PARTICLE), shader -> { SHATTER_OUTLINE = shader; });
    }
}
