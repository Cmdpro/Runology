package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class RunicConstructRenderer extends GeoEntityRenderer<RunicConstruct> {
    public RunicConstructRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RunicConstructModel());
        this.shadowRadius = 0.5f;
    }
    @Override
    public ResourceLocation getTextureLocation(RunicConstruct instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicconstruct.png");
    }

    @Override
    public RenderType getRenderType(RunicConstruct animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}