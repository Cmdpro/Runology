package com.cmdpro.runology.renderers.block;

import com.cmdpro.databank.ClientDatabankUtils;
import com.cmdpro.databank.misc.DebugHelper;
import com.cmdpro.databank.multiblock.MultiblockRenderer;
import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;

public class ShatterRenderer implements BlockEntityRenderer<ShatterBlockEntity> {
    @Override
    public void render(ShatterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!ClientDatabankUtils.isDrawRenderTarget(Minecraft.getInstance().getMainRenderTarget())) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Vec3 offset = Vec3.ZERO;
        if (blockEntity.instabilityExplardTimer < ShatterBlockEntity.INSTABILITY_EXPLARD_TIME) {
            float explardProgress = 1f-(((float)blockEntity.instabilityExplardTimer-partialTick)/(float)ShatterBlockEntity.INSTABILITY_EXPLARD_TIME);
            float distance = 0.3f*explardProgress;
            float ySpeed = 20;
            float xSpeed = (ySpeed*1.5f);
            float time = (float)(Blaze3D.getTime()*360f);
            offset = Vec3.directionFromRotation(Math.cos(time*ySpeed)*180, Math.sin(time*xSpeed)*180).scale(distance);
            if (explardProgress >= 0.9) {
                float scale = 1f-Math.clamp(0f, 1f, (explardProgress-0.9f)*20f);
                poseStack.scale(scale, scale, scale);
            }
        }
        if (blockEntity.instabilityExplardTimer < ShatterBlockEntity.INSTABILITY_EXPLARD_TIME) {
            int lineCount = 20;
            Random random = new Random(0);
            for (int i = 0; i < lineCount; i++) {
                float explardProgress = 1f - ((float) blockEntity.instabilityExplardTimer / (float) ShatterBlockEntity.INSTABILITY_EXPLARD_TIME);
                float distance = Math.clamp(0f, 4f, 6f * explardProgress) * explardProgress;
                float time = (float) (Blaze3D.getTime() * 360f);
                float rotationPitch = (time / 3) * Math.sin(random.nextFloat()*360f);
                float rotationYaw = (time / 3) * Math.sin(random.nextFloat()*360f);
                Vec3 dir = Vec3.directionFromRotation((random.nextFloat()*360f) + rotationPitch, (random.nextFloat()*360f) + rotationYaw);
                Vec3 end = dir.scale(Math.sin(Math.toRadians((time / 2f)+(random.nextFloat()*360f))) * distance);
                renderSpike(Vec3.ZERO, end, Math.clamp(0f, 0.3f, 0.6f * explardProgress), poseStack);
            }
        }
        poseStack.translate(offset.x, offset.y, offset.z);
        renderSpikes(partialTick, poseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER), 1.5f);
        renderSpikes(partialTick, poseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER), 1.5f);
        poseStack.popPose();
    }
    private void renderLine(Vec3 start, Vec3 end, float thickness, PoseStack poseStack) {
        renderLine(start, end, thickness, poseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
        renderLine(start, end, thickness, poseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
    }
    private void renderSpike(Vec3 start, Vec3 end, float thickness, PoseStack poseStack) {
        renderSpike(start, end, thickness, poseStack, RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
        renderSpike(start, end, thickness, poseStack, RenderEvents.createShatterInsideBufferSource().getBuffer(RunologyRenderTypes.SHATTER));
    }
    private void renderLine(Vec3 start, Vec3 end, float thickness, PoseStack poseStack, VertexConsumer vertexConsumer) {
        Vector3f diff = end.toVector3f().sub(start.toVector3f()).normalize().mul(thickness, thickness, thickness);
        Vector3f diffRotated = new Vector3f(diff).rotateX(Math.toRadians(90));
        for (int i = 0; i < 4; i++) {
            Vector3f offset = new Vector3f(diffRotated).rotateY(Math.toRadians(90*i));
            Vector3f offset2 = new Vector3f(diffRotated).rotateY(Math.toRadians(90*(i+1)));
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset2));
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f().add(offset));
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        }
        Vector3f offset = new Vector3f(diffRotated);
        Vector3f offset2 = new Vector3f(diffRotated).rotateY(Math.toRadians(90));
        Vector3f offset3 = new Vector3f(diffRotated).rotateY(Math.toRadians(180));
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

    public static class SpikeData {
        public Vec3 rotOffset;
        public float size;
        protected SpikeData(Vec3 rotOffset, float size) {
            this.rotOffset = rotOffset;
            this.size = size;
        }
    }
    public static List<SpikeData> rotOffsets = new ArrayList<>();

    static {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            rotOffsets.add(new SpikeData(new Vec3((rand.nextFloat() * 6f) - 3f, (rand.nextFloat() * 6f) - 3f, (rand.nextFloat() * 6f) - 3f), (rand.nextFloat() * 0.15f) + 0.1f));
        }
    }

    EntityRenderDispatcher renderDispatcher;
    public ShatterRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        renderDispatcher = rendererProvider.getEntityRenderer();
    }
    public static void renderSpikes(float partialTick, PoseStack poseStack, VertexConsumer vertexConsumer, float scaled) {
        float rotatyThing = Math.toRadians(Minecraft.getInstance().level.getGameTime() + partialTick)*10f;
        int ind = 0;
        for (SpikeData i : rotOffsets) {
            float xMult = (float)i.rotOffset.x;
            float yMult = (float)i.rotOffset.y;
            float zMult = (float)i.rotOffset.z;
            float size = i.size+(Math.sin((rotatyThing/20f)+(ind*3))*0.05f);
            poseStack.pushPose();
            poseStack.scale(scaled, scaled, scaled);
            poseStack.mulPose(Axis.XP.rotation((rotatyThing * xMult) * Mth.DEG_TO_RAD));
            poseStack.mulPose(Axis.YP.rotation((rotatyThing * yMult) * Mth.DEG_TO_RAD));
            poseStack.mulPose(Axis.ZP.rotation((rotatyThing * zMult) * Mth.DEG_TO_RAD));
            renderSpike(new Vec3(0, 0, 0), new Vec3(0, (i.size-(Math.sin((rotatyThing/30f)+(ind*3))*0.05f))*3f, 0), size, poseStack, vertexConsumer);
            poseStack.popPose();
            ind++;
        }
    }
    private static void renderSpike(Vec3 start, Vec3 end, float thickness, PoseStack poseStack, VertexConsumer vertexConsumer) {
        Vector3f diff = end.toVector3f().sub(start.toVector3f()).normalize().mul(thickness, thickness, thickness);
        Vector3f diffRotated = new Vector3f(diff).rotateX(Math.toRadians(90));
        for (int i = 0; i < 4; i++) {
            Vector3f offset = new Vector3f(diffRotated).rotateY(Math.toRadians(90*i));
            Vector3f offset2 = new Vector3f(diffRotated).rotateY(Math.toRadians(90*(i+1)));
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f());
            vertexConsumer.addVertex(poseStack.last(), end.toVector3f());
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));
            vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        }
        Vector3f offset = new Vector3f(diffRotated);
        Vector3f offset2 = new Vector3f(diffRotated).rotateY(Math.toRadians(90));
        Vector3f offset3 = new Vector3f(diffRotated).rotateY(Math.toRadians(180));
        Vector3f offset4 = new Vector3f(diffRotated).rotateY(Math.toRadians(270));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset4));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset3));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset2));
        vertexConsumer.addVertex(poseStack.last(), start.toVector3f().add(offset));
    }

}
