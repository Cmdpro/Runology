package com.cmdpro.runology.shaders;

import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.Runology;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

public class ShatteredSkyShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return Runology.locate("shaders/post/shattered_sky.json");
    }
    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);
        instance.getEffect().setSampler("ShatteredSkySampler", RenderEvents.getShatteredSkyTarget()::getColorTextureId);
        instance.getEffect().setSampler("ShatteredSkyBgSampler", RenderEvents.getShatteredSkyBgTarget()::getColorTextureId);
        instance.getEffect().safeGetUniform("FogStart").set(RenderHandler.fogStart);
        instance.getEffect().safeGetUniform("FogEnd").set(RenderEvents.fogEnd);
    }

}
