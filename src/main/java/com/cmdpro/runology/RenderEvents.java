package com.cmdpro.runology;

import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.SequencedMap;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID)
public class RenderEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            Matrix4f oldMat = new Matrix4f(RenderSystem.getModelViewMatrix());
            RenderSystem.getModelViewMatrix().set(RenderHandler.matrix4f);
            shatterTarget.clear(Minecraft.ON_OSX);
            shatterTarget.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            Minecraft.getInstance().getMainRenderTarget().unbindWrite();
            getShatterTarget().bindWrite(true);
            createShatterOutlineBufferSource().endBatch(RunologyRenderTypes.SHATTER);
            getShatterTarget().unbindWrite();
            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
            RenderSystem.getModelViewMatrix().set(oldMat);
        }
    }
    private static RenderTarget shatterTarget;
    public static RenderTarget getShatterTarget() {
        if (shatterTarget == null) {
            shatterTarget = new MainTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
            shatterTarget.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return shatterTarget;
    }
    static MultiBufferSource.BufferSource shatterOutlineBufferSource = null;
    public static MultiBufferSource.BufferSource createShatterOutlineBufferSource() {
        if (shatterOutlineBufferSource == null) {
            SequencedMap<RenderType, ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(RunologyRenderTypes.SHATTER, new ByteBufferBuilder(RunologyRenderTypes.SHATTER.bufferSize));
            shatterOutlineBufferSource = MultiBufferSource.immediateWithBuffers(buffers, new ByteBufferBuilder(256));
        }
        return shatterOutlineBufferSource;
    }
}
