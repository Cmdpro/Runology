package com.cmdpro.runology.mixin.client;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import com.cmdpro.runology.RenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "resize", at = @At(value = "TAIL"), remap = false)
    private void Runology$resize(int pWidth, int pHeight, CallbackInfo ci) {
        RenderEvents.getShatterTarget().resize(pWidth, pHeight, Minecraft.ON_OSX);
    }
}
