package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.VoidBomb;
import com.cmdpro.runology.entity.VoidBullet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class VoidBulletRenderer extends GeoEntityRenderer<VoidBullet> {
    public VoidBulletRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Model());
        this.shadowRadius = 0.5f;
    }
    @Override
    public ResourceLocation getTextureLocation(VoidBullet instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/voidbullet.png");
    }

    @Override
    public RenderType getRenderType(VoidBullet animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class Model extends GeoModel<VoidBullet> {
        @Override
        public ResourceLocation getModelResource(VoidBullet object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/voidbullet.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(VoidBullet object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/voidbullet.png");
        }

        @Override
        public ResourceLocation getAnimationResource(VoidBullet animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/voidbullet.animation.json");
        }
    }
}
