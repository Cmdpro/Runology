package com.cmdpro.runology.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;

public class ClientRunologyUtil {
    public static void drawSphere(VFXBuilders.WorldVFXBuilder builder, PoseStack stack, Color color, float alpha, float radius, int longs, int lats) {
        builder.setPosColorTexLightmapDefaultFormat().setColor(color).setAlpha(alpha).renderSphere(RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyWithModifierAndCache(new ResourceLocation("textures/misc/white.png"), b -> b.replaceVertexFormat(VertexFormat.Mode.TRIANGLES).setCullState(LodestoneRenderTypeRegistry.NO_CULL))), stack, radius, longs, lats);
    }
}
