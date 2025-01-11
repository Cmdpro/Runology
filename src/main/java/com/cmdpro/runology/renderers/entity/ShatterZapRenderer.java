package com.cmdpro.runology.renderers.entity;

import com.cmdpro.databank.ClientDatabankUtils;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.entity.ShatterZap;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class ShatterZapRenderer extends EntityRenderer<ShatterZap> {

    public ShatterZapRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public ResourceLocation getTextureLocation(ShatterZap pEntity) {
        return null;
    }
    @Override
    public void render(ShatterZap pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.victimPos == null) {
            return;
        }
        Vector3f vector3f = pEntity.victimPos;
        Vec3 pos = new Vec3(vector3f.x, vector3f.y, vector3f.z);
        double length = pEntity.position().distanceTo(pos);
        Vec3 lastPos = pEntity.position();
        List<Map.Entry<Float, Vec2>> entries = pEntity.offsets.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).toList();
        for (Map.Entry<Float, Vec2> i : entries) {
            double distance = i.getKey().doubleValue();
            Vec3 pos2 = pEntity.position().lerp(pos, distance/length);
            Vec2 rotVec = calculateRotationVector(pEntity.position(), pos);
            Vec3 offset = pEntity.calculateViewVector(i.getValue().x, rotVec.y-90).multiply(i.getValue().y, i.getValue().y, i.getValue().y);
            pos2 = pos2.add(offset);
            pPoseStack.pushPose();
            pPoseStack.translate(lastPos.x-pEntity.position().x, lastPos.y-pEntity.position().y, lastPos.z-pEntity.position().z);
            pPoseStack.pushPose();
            ClientDatabankUtils.rotateStackToPoint(pPoseStack, lastPos, pos2);
            renderLine(Vec3.ZERO, new Vec3(0, lastPos.distanceTo(pos2), 0), (1f - ((float) pEntity.time / 20f))*0.05f, pPartialTick, pPoseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
            renderLine(Vec3.ZERO, new Vec3(0, lastPos.distanceTo(pos2), 0), (1f - ((float) pEntity.time / 20f))*0.05f, pPartialTick, pPoseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
            pPoseStack.popPose();
            pPoseStack.popPose();
            lastPos = pos2;
        }
        pPoseStack.pushPose();
        pPoseStack.translate(lastPos.x-pEntity.position().x, lastPos.y-pEntity.position().y, lastPos.z-pEntity.position().z);
        pPoseStack.pushPose();
        ClientDatabankUtils.rotateStackToPoint(pPoseStack, lastPos, pos);
        renderLine(Vec3.ZERO, new Vec3(0, lastPos.distanceTo(pos), 0), (1f - ((float) pEntity.time / 20f))*0.05f, pPartialTick, pPoseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
        renderLine(Vec3.ZERO, new Vec3(0, lastPos.distanceTo(pos), 0), (1f - ((float) pEntity.time / 20f))*0.05f, pPartialTick, pPoseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
        pPoseStack.popPose();
        pPoseStack.popPose();
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
        Vector3f offset4 = new Vector3f(diffRotated).rotateY(org.joml.Math.toRadians(270));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset4));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset3));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));

        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset2));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset3));
        vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset4));
    }
    public Vec2 calculateRotationVector(Vec3 pVec, Vec3 pTarget) {
        double d0 = pTarget.x - pVec.x;
        double d1 = pTarget.y - pVec.y;
        double d2 = pTarget.z - pVec.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return new Vec2(
                Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)))),
                Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F)
        );
    }

    @Override
    public boolean shouldRender(ShatterZap pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}