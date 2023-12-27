package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.entity.BillboardProjectile;
import com.cmdpro.runicarts.entity.SoulRitualController;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class SoulRitualControllerRenderer extends EntityRenderer<SoulRitualController> {

    public SoulRitualControllerRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(SoulRitualController pEntity) {
        return null;
    }
}