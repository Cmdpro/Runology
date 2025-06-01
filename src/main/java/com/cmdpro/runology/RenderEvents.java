package com.cmdpro.runology;

import com.cmdpro.databank.mixin.client.BufferSourceMixin;
import com.cmdpro.databank.multiblock.MultiblockRenderer;
import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.databank.rendering.ShaderHelper;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.SequencedMap;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID)
public class RenderEvents {
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_PARTICLES)) {
        }
        if (ShaderHelper.shouldUseAlternateRendering()) {
            if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_LEVEL)) {
                RenderSystem.getModelViewStack().pushMatrix().set(RenderHandler.matrix4f);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setShaderFogStart(RenderHandler.fogStart);
                doEffectRendering(event);
                FogRenderer.setupNoFog();
                RenderSystem.getModelViewStack().popMatrix();
                RenderSystem.applyModelViewMatrix();
            }
        } else {
            if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
                doEffectRendering(event);
            }
        }
    }
    private static void doEffectRendering(RenderLevelStageEvent event) {
        RenderSystem.depthMask(true);
        getSpecialBypassTarget().clear(Minecraft.ON_OSX);
        getSpecialBypassTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        createShatterInsideBufferSource().endBatch();
        getShatterTarget().clear(Minecraft.ON_OSX);
        getShatterTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        getPlayerPowerTarget().clear(Minecraft.ON_OSX);
        getPlayerPowerTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        getShatterTarget().bindWrite(true);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.SHATTER);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.SHATTER_PARTICLE);
        getPlayerPowerTarget().bindWrite(true);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.PLAYER_POWER);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.PLAYER_POWER_PARTICLE);
        getSpecialBypassTarget().bindWrite(true);
        createSpecialBypassBufferSource().endBatch();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        RenderSystem.depthMask(false);
    }
    private static RenderTarget shatterTarget;
    public static RenderTarget getShatterTarget() {
        if (shatterTarget == null) {
            shatterTarget = new MainTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
            shatterTarget.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return shatterTarget;
    }
    private static RenderTarget specialBypassTarget;
    public static RenderTarget getSpecialBypassTarget() {
        if (specialBypassTarget == null) {
            specialBypassTarget = new MainTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
            specialBypassTarget.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return specialBypassTarget;
    }
    private static RenderTarget playerPowerTarget;
    public static RenderTarget getPlayerPowerTarget() {
        if (playerPowerTarget == null) {
            playerPowerTarget = new MainTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
            playerPowerTarget.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return playerPowerTarget;
    }
    static MultiBufferSource.BufferSource shatterOutlineBufferSource = null;
    public static MultiBufferSource.BufferSource createShatterOutlineBufferSource() {
        if (shatterOutlineBufferSource == null) {
            SequencedMap<RenderType, ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(RunologyRenderTypes.SHATTER, new ByteBufferBuilder(RunologyRenderTypes.SHATTER.bufferSize));
            buffers.put(RunologyRenderTypes.SHATTER_PARTICLE, new ByteBufferBuilder(RunologyRenderTypes.SHATTER_PARTICLE.bufferSize));
            buffers.put(RunologyRenderTypes.PLAYER_POWER, new ByteBufferBuilder(RunologyRenderTypes.PLAYER_POWER.bufferSize));
            buffers.put(RunologyRenderTypes.PLAYER_POWER_PARTICLE, new ByteBufferBuilder(RunologyRenderTypes.PLAYER_POWER_PARTICLE.bufferSize));
            shatterOutlineBufferSource = MultiBufferSource.immediateWithBuffers(buffers, new ByteBufferBuilder(256));
        }
        return shatterOutlineBufferSource;
    }
    static MultiBufferSource.BufferSource shatterInsideBufferSource = null;
    public static MultiBufferSource.BufferSource createShatterInsideBufferSource() {
        if (shatterInsideBufferSource == null) {
            SequencedMap<RenderType, ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(RunologyRenderTypes.SHATTER, new ByteBufferBuilder(RunologyRenderTypes.SHATTER.bufferSize));
            buffers.put(RunologyRenderTypes.SHATTER_PARTICLE, new ByteBufferBuilder(RunologyRenderTypes.SHATTER_PARTICLE.bufferSize));
            buffers.put(RunologyRenderTypes.PLAYER_POWER, new ByteBufferBuilder(RunologyRenderTypes.PLAYER_POWER.bufferSize));
            buffers.put(RunologyRenderTypes.PLAYER_POWER_PARTICLE, new ByteBufferBuilder(RunologyRenderTypes.PLAYER_POWER_PARTICLE.bufferSize));
            shatterInsideBufferSource = MultiBufferSource.immediateWithBuffers(buffers, new ByteBufferBuilder(256));
        }
        return shatterInsideBufferSource;
    }
    static MultiBufferSource.BufferSource specialBypassBufferSource = null;
    public static MultiBufferSource.BufferSource createSpecialBypassBufferSource() {
        if (specialBypassBufferSource == null) {
            specialBypassBufferSource = new SpecialBypassBuffers(((BufferSourceMixin)Minecraft.getInstance().renderBuffers().bufferSource()).getSharedBuffer());
        }
        return specialBypassBufferSource;
    }
    private static class SpecialBypassBuffers extends MultiBufferSource.BufferSource {
        protected SpecialBypassBuffers(ByteBufferBuilder fallback) {
            super(fallback, new Object2ObjectLinkedOpenHashMap<>());
        }

        @Override
        public VertexConsumer getBuffer(RenderType type) {
            if (!fixedBuffers.containsKey(type)) {
                fixedBuffers.put(type, new ByteBufferBuilder(type.bufferSize));
            }
            return super.getBuffer(type);
        }
    }
}
