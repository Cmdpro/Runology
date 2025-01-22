package com.cmdpro.runology;

import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.databank.rendering.ShaderHelper;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
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
        createShatterInsideBufferSource().endBatch();
        getShatterTarget().clear(Minecraft.ON_OSX);
        getShatterTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        getPlayerPowerTarget().clear(Minecraft.ON_OSX);
        getPlayerPowerTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        getShatterTarget().bindWrite(false);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.SHATTER);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.SHATTER_PARTICLE);
        getPlayerPowerTarget().bindWrite(false);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.PLAYER_POWER);
        createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.PLAYER_POWER_PARTICLE);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
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
}
