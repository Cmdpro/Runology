package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.Shatter;
import com.cmdpro.runology.entity.Totem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class TotemRenderer extends GeoEntityRenderer<Totem> {
    public TotemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TotemModel());
        this.shadowRadius = 1f;
    }

    @Override
    public ResourceLocation getTextureLocation(Totem instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/totem.png");
    }

    @Override
    public RenderType getRenderType(Totem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class TotemModel extends GeoModel<Totem> {
        @Override
        public ResourceLocation getModelResource(Totem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/totem.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(Totem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/totem.png");
        }

        @Override
        public ResourceLocation getAnimationResource(Totem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/totem.animation.json");
        }
    }
}
