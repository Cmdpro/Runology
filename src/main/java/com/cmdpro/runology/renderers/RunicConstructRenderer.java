package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;


public class RunicConstructRenderer extends GeoEntityRenderer<RunicConstruct> {
    public RunicConstructRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Model());
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
    public static class Model extends GeoModel<RunicConstruct> {
        @Override
        public ResourceLocation getModelResource(RunicConstruct object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runicconstruct.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicConstruct object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicconstruct.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicConstruct animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runicconstruct.animation.json");
        }
    }
}
