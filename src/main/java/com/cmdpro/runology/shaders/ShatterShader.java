package com.cmdpro.runology.shaders;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.Runology;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

public class ShatterShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shaders/post/shatter_post.json");
    }
    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);
        instance.getEffect().setSampler("ShatterSampler", RenderEvents.getShatterTarget()::getColorTextureId);
    }
}
