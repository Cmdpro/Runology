package com.cmdpro.runology.renderers.entity;

import com.cmdpro.databank.ClientDatabankUtils;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.entity.RunicCodexEntry;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RunicCodexEntryRenderer extends EntityRenderer<RunicCodexEntry> {
    public RunicCodexEntryRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(RunicCodexEntry entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        for (var i : entity.parentEntityLocations) {
            poseStack.pushPose();
            poseStack.translate(0, entity.getBoundingBox().getYsize()/2, 0);
            ClientDatabankUtils.rotateStackToPoint(poseStack, entity.getBoundingBox().getCenter(), i);
            renderLine(Vec3.ZERO, new Vec3(0, entity.getBoundingBox().getCenter().distanceTo(i), 0), 0.05f, partialTick, poseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
            renderLine(Vec3.ZERO, new Vec3(0, entity.getBoundingBox().getCenter().distanceTo(i), 0), 0.05f, partialTick, poseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
            poseStack.popPose();
        }
        if (entity.icon != null) {
            poseStack.pushPose();
            poseStack.translate(0, entity.getBoundingBox().getYsize() / 2, 0);
            poseStack.scale(0.2f, 0.2f, 0.2f);
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.icon, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, RenderEvents.createSpecialBypassBufferSource(), entity.level(), 0);
            poseStack.popPose();
        }
    }
    private void renderLine(Vec3 start, Vec3 end, float thickness, float partialTick, PoseStack poseStack, VertexConsumer vertexConsumer) {
        Vector3f diff = end.toVector3f().sub(start.toVector3f()).normalize().mul(thickness, thickness, thickness);
        Vector3f diffRotated = new Vector3f(diff).rotateX(org.joml.Math.toRadians(90));
        for (int i = 0; i < 4; i++) {
            Vector3f offset = new Vector3f(diffRotated).rotateY(org.joml.Math.toRadians(90*i));
            Vector3f offset2 = new Vector3f(diffRotated).rotateY(org.joml.Math.toRadians(90*(i+1)));
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset2));
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset));
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        }
        Vector3f offset = new Vector3f(diffRotated);
        Vector3f offset2 = new Vector3f(diffRotated).rotateY(org.joml.Math.toRadians(90));
        Vector3f offset3 = new Vector3f(diffRotated).rotateY(org.joml.Math.toRadians(180));
        Vector3f offset4 = new Vector3f(diffRotated).rotateY(Math.toRadians(270));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset4));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset3));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));

        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset2));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset3));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset4));
    }

    @Override
    public ResourceLocation getTextureLocation(RunicCodexEntry entity) {
        return null;
    }
}
