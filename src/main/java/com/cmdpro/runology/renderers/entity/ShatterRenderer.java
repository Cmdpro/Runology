package com.cmdpro.runology.renderers.entity;

import com.cmdpro.runology.entity.Shatter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;

public class ShatterRenderer extends EntityRenderer<Shatter> {
    private class SpikeData {
        public Vec3 rotOffset;
        public float size;
        public SpikeData(Vec3 rotOffset, float size) {
            this.rotOffset = rotOffset;
            this.size = size;
        }
    }
    public List<SpikeData> rotOffsets = new ArrayList<>();

    public ShatterRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            rotOffsets.add(new SpikeData(new Vec3((rand.nextFloat()*6f)-3f, (rand.nextFloat()*6f)-3f, (rand.nextFloat()*6f)-3f), (rand.nextFloat()*0.15f)+0.1f));
        }
    }
    public ResourceLocation getTextureLocation(Shatter pEntity) {
        return null;
    }

    @Override
    public void render(Shatter entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        float rotatyThing = Math.toRadians(Minecraft.getInstance().level.getGameTime() + partialTick)*10f;
        int ind = 0;
        for (SpikeData i : rotOffsets) {
            float xMult = (float)i.rotOffset.x;
            float yMult = (float)i.rotOffset.y;
            float zMult = (float)i.rotOffset.z;
            float size = i.size+(Math.sin((rotatyThing/20f)+(ind*3))*0.05f);
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            poseStack.mulPose(Axis.XP.rotation((rotatyThing * xMult) * Mth.DEG_TO_RAD));
            poseStack.mulPose(Axis.YP.rotation((rotatyThing * yMult) * Mth.DEG_TO_RAD));
            poseStack.mulPose(Axis.ZP.rotation((rotatyThing * zMult) * Mth.DEG_TO_RAD));
            renderSpike(new Vec3(0, 0, 0), new Vec3(0, (i.size-(Math.sin((rotatyThing/30f)+(ind*3))*0.05f))*3f, 0), size, partialTick, poseStack, bufferSource, packedLight);
            poseStack.popPose();
            ind++;
        }
    }
    private void renderSpike(Vec3 start, Vec3 end, float thickness, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Vector3f diff = end.toVector3f().sub(start.toVector3f()).normalize().mul(thickness, thickness, thickness);
        Vector3f diffRotated = new Vector3f(diff).rotateX(Math.toRadians(90));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.endPortal());
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