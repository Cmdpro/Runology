package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.Shatter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;


public class ShatterRenderer extends GeoEntityRenderer<Shatter> {
    public ShatterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShatterModel());
        this.shadowRadius = 1f;
    }
    @Override
    public void renderRecursively(PoseStack poseStack, Shatter animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getParent() != null && bone.getParent().getName().equals("outer") && bone.getName().equals("inner2")) {
            if (animatable.getEntityData().get(Shatter.OPENED)) {
                super.renderRecursively(poseStack, animatable, bone, RenderType.endGateway(), bufferSource, bufferSource.getBuffer(RenderType.endGateway()), isReRender, partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            }
        } else {
            RenderType renderType2 = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);
            super.renderRecursively(poseStack, animatable, bone, renderType2, bufferSource, bufferSource.getBuffer(renderType2), isReRender, partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
        }
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, Shatter animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        super.scaleModelForRender(widthScale*2, heightScale*2, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public ResourceLocation getTextureLocation(Shatter instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/shatter.png");
    }

    @Override
    public RenderType getRenderType(Shatter animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class ShatterModel extends GeoModel<Shatter> {
        @Override
        public ResourceLocation getModelResource(Shatter object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/shatter.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(Shatter object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/shatter.png");
        }

        @Override
        public ResourceLocation getAnimationResource(Shatter animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/shatter.animation.json");
        }
        @Override
        public void setCustomAnimations(Shatter animatable, long instanceId, AnimationState<Shatter> animationState) {
            CoreGeoBone head = getAnimationProcessor().getBone("outer");

            if (head != null) {
                Vec3 vec3 = animatable.position().add(0, animatable.getBoundingBox().getYsize()/2, 0);
                Vec3 pTarget = Minecraft.getInstance().player.getEyePosition();
                double d0 = pTarget.x - vec3.x;
                double d1 = pTarget.y - vec3.y;
                double d2 = pTarget.z - vec3.z;
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                head.setRotX(-Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)))) * Mth.DEG_TO_RAD);
                head.setRotY(-Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F) * Mth.DEG_TO_RAD);
            }
        }
    }
}
