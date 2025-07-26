package com.cmdpro.runology.networking.packet;

import com.cmdpro.databank.impact.ImpactFrameHandler;
import com.cmdpro.databank.misc.FloatGradient;
import com.cmdpro.databank.misc.ScreenshakeHandler;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.data.entries.*;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.renderers.block.ShatterRenderer;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

import java.util.Map;

public record ShatterExplodeS2CPacket(BlockPos pos) implements Message {
    public static ShatterExplodeS2CPacket read(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new ShatterExplodeS2CPacket(pos);
    }

    public static void write(RegistryFriendlyByteBuf buf, ShatterExplodeS2CPacket obj) {
        buf.writeBlockPos(obj.pos);
    }
    @Override
    public void handleClient(Minecraft minecraft, Player player, IPayloadContext ctx) {
        ClientHandler.handle(this, player, ctx);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<ShatterExplodeS2CPacket> TYPE = new Type<>(Runology.locate("shatter_explode"));

    private static class ClientHandler {
        public static void handle(ShatterExplodeS2CPacket packet, Player player, IPayloadContext ctx) {
            BlockPos pos = packet.pos;
            ctx.enqueueWork(() -> {
                ImpactFrameHandler.addImpact(6, (renderTarget, buffer, poseStack, deltaTracker, camera, frustum, renderTicks, progress) -> {

                }, (renderTarget, buffer, poseStack, deltaTracker, camera, frustum, renderTicks, progress) -> {
                    Vec3 center = pos.getCenter();
                    poseStack.pushPose();
                    poseStack.translate(center.x, center.y, center.z);
                    ShatterRenderer.renderSpikes(deltaTracker.getGameTimeDeltaPartialTick(false), poseStack, buffer.getBuffer(RunologyRenderTypes.SHATTER), 1.5f);
                    poseStack.popPose();
                    poseStack.pushPose();
                    poseStack.translate(center.x, center.y, center.z);
                    float shift = 10f;
                    float modifiedProgress = progress;
                    float slowStart = 0.5f;
                    float slowEnd = 0.75f;
                    float slowRange = 1f-slowStart;
                    if (progress >= slowStart) {
                        float slowProgress = (progress-slowStart)/slowRange;
                        modifiedProgress = slowStart+((slowEnd-slowStart)*slowProgress);
                    }
                    float scaleAddition = modifiedProgress*shift;
                    for (int i = 0; i < 16; i++) {
                        double angle = Math.toRadians((360f/16f)*i);
                        Vec3 dir = new Vec3(Math.sin(angle), 0, Math.cos(angle));
                        renderLine(dir.scale(Math.clamp(scaleAddition-2.5f, 0, scaleAddition)), dir.scale(scaleAddition), 0.05f, poseStack, buffer.getBuffer(RunologyRenderTypes.SHATTER));
                    }
                    for (int i = 0; i < 16; i++) {
                        double angle = Math.toRadians((360f/16f)*i);
                        Vec3 dir = new Vec3(0, Math.sin(angle), Math.cos(angle));
                        renderLine(dir.scale(Math.clamp(scaleAddition-2.5f, 0, scaleAddition)), dir.scale(scaleAddition), 0.05f, poseStack, buffer.getBuffer(RunologyRenderTypes.SHATTER));
                    }
                    for (int i = 0; i < 16; i++) {
                        double angle = Math.toRadians((360f/16f)*i);
                        Vec3 dir = new Vec3(Math.cos(angle), Math.sin(angle), 0);
                        renderLine(dir.scale(Math.clamp(scaleAddition-2.5f, 0, scaleAddition)), dir.scale(scaleAddition), 0.05f, poseStack, buffer.getBuffer(RunologyRenderTypes.SHATTER));
                    }
                    poseStack.popPose();
                }, FloatGradient.singleValue(1f).fade(1, 0.7f, 0f, 1f));
                ScreenshakeHandler.addScreenshake(12, 3f);
            });
            Vec3 center = pos.getCenter();
            float particleSpeed = 0.9f;
            for (int i = 0; i < 90; i++) {
                double angle = Math.toRadians((360f/90f)*i);
                player.level().addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, Math.sin(angle)*particleSpeed, 0, Math.cos(angle)*particleSpeed);
            }
        }
        private static void renderLine(Vec3 start, Vec3 end, float thickness, PoseStack poseStack, VertexConsumer vertexConsumer) {
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
    }
}