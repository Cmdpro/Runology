package com.cmdpro.runology.shaders;

import com.cmdpro.runology.Runology;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RunologyCoreShaders {
    public static ShaderInstance SHATTER;
    public static ShaderInstance getShatter() {
        return SHATTER;
    }
    public static ShaderInstance SHATTER_PARTICLE;
    public static ShaderInstance getShatterParticle() {
        return SHATTER_PARTICLE;
    }
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter"), DefaultVertexFormat.POSITION), shader -> { SHATTER = shader; });
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter_particle"), DefaultVertexFormat.PARTICLE), shader -> { SHATTER_PARTICLE = shader; });
    }
}
