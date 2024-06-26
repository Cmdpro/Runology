package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.RunicScout;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class RunicScoutRenderer extends GeoEntityRenderer<RunicScout> {
    public RunicScoutRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Model());
        this.shadowRadius = 0.1f;
    }
    @Override
    public ResourceLocation getTextureLocation(RunicScout instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicscout.png");
    }

    @Override
    public RenderType getRenderType(RunicScout animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class Model extends GeoModel<RunicScout> {
        @Override
        public ResourceLocation getModelResource(RunicScout object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runicscout.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicScout object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicscout.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicScout animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runicscout.animation.json");
        }
    }
}
