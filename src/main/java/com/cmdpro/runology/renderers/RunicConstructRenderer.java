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
        super(renderManager, new RunicConstructModel());
        this.shadowRadius = 0.5f;
    }
/*
    @Override
    public void renderRecursively(PoseStack poseStack, RunicConstruct animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getParent() != null && bone.getParent().getName().equals("body") && bone.getName().equals("bone2")) {
            super.renderRecursively(poseStack, animatable, bone, RenderType.endGateway(), bufferSource, bufferSource.getBuffer(RenderType.endGateway()), isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            RenderType renderType2 = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);
            super.renderRecursively(poseStack, animatable, bone, renderType2, bufferSource, bufferSource.getBuffer(renderType2), isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }*/

    @Override
    public ResourceLocation getTextureLocation(RunicConstruct instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicconstruct.png");
    }

    @Override
    public RenderType getRenderType(RunicConstruct animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class RunicConstructModel extends GeoModel<RunicConstruct> {
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
