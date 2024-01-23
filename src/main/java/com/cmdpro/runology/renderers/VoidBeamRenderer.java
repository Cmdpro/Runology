package com.cmdpro.runology.renderers;

import com.cmdpro.runology.entity.BillboardProjectile;
import com.cmdpro.runology.entity.VoidBeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class VoidBeamRenderer extends EntityRenderer<VoidBeam> {

    public VoidBeamRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
        this.shadowRadius = 0f;
    }
    @Override
    protected int getBlockLightLevel(VoidBeam p_114087_, BlockPos p_114088_) {
        return 15;
    }

    @Override
    public void render(VoidBeam pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.translate(-0.5f, 0, -0.5f);
        float size = pEntity.time >= 110 ? (1f-(((float)pEntity.time-110)/10f)) : 0.1f;
        BeaconRenderer.renderBeaconBeam(pPoseStack, pBuffer, BeaconRenderer.BEAM_LOCATION, pPartialTick, 1.0F, Minecraft.getInstance().level.getGameTime(), 0, 100, new float[] { 1f, 0f, 1f }, size, size);
        pPoseStack.translate(0.5f, 0, 0.5f);
    }
    @Override
    public ResourceLocation getTextureLocation(VoidBeam proj) {
        return null;
    }
}