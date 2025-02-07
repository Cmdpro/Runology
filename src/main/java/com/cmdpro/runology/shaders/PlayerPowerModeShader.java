package com.cmdpro.runology.shaders;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.Runology;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

public class PlayerPowerModeShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return Runology.locate("shaders/post/player_power_mode.json");
    }
    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);
    }
}
