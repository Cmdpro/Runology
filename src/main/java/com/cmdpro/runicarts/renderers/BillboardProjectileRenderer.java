package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.entity.BillboardProjectile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class BillboardProjectileRenderer extends EntityRenderer<BillboardProjectile> {

    public BillboardProjectileRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
        this.shadowRadius = 0.5f;
    }

    protected int getBlockLightLevel(BillboardProjectile p_114087_, BlockPos p_114088_) {
        return 15;
    }

    public void render(BillboardProjectile proj, float p_114081_, float p_114082_, PoseStack stack, MultiBufferSource p_114084_, int p_114085_) {
        stack.pushPose();
        stack.translate(0, proj.getOffset()*proj.getScale(), 0);
        stack.scale(2, 2, 2);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.scale(proj.getScale(), proj.getScale(), proj.getScale());
        PoseStack.Pose posestack$pose = stack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RenderType.entityTranslucent(getTextureLocation(proj)));
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1, proj.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1, proj.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0, proj.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0, proj.getColor());
        stack.popPose();
        super.render(proj, p_114081_, p_114082_, stack, p_114084_, p_114085_);
    }

    private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_, Color color) {
        p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.5F, 0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }
    public ResourceLocation getTextureLocation(BillboardProjectile proj) {
        return proj.getSprite();
    }
}