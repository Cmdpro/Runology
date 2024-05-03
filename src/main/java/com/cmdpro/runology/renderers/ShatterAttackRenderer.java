package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.BillboardProjectile;
import com.cmdpro.runology.entity.IceShard;
import com.cmdpro.runology.entity.ShatterAttack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShatterAttackRenderer extends EntityRenderer<ShatterAttack> {

    public ShatterAttackRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public ResourceLocation getTextureLocation(ShatterAttack pEntity) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/shatterattack.png");
    }
    @Override
    public void render(ShatterAttack pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.victimPos == null) {
            return;
        }
        pPoseStack.pushPose();
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld();
        LodestoneRenderType renderType = LodestoneRenderTypeRegistry.TRANSPARENT_TEXTURE.applyAndCache(getTextureLocation(pEntity));
        pPoseStack.translate(-pEntity.position().x, -pEntity.position().y, -pEntity.position().z);
        Vector3f vector3f = pEntity.victimPos;
        Vec3 pos = new Vec3(vector3f.x, vector3f.y, vector3f.z);
        double length = pEntity.position().distanceTo(pos);
        Vec3 lastPos = pEntity.position();
        List<Map.Entry<Float, Vec2>> entries = pEntity.offsets.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).toList();
        for (Map.Entry<Float, Vec2> i : entries) {
            double distance = i.getKey().doubleValue();
            Vec3 pos2 = pEntity.position().lerp(pos, distance/length);
            Vec2 rotVec = calculateRotationVector(pEntity.position(), pos);
            Vec3 offset = calculateViewVector(i.getValue().x, rotVec.y-90).multiply(i.getValue().y, i.getValue().y, i.getValue().y);
            pos2 = pos2.add(offset);
            builder.setAlpha(1f - ((float) pEntity.time / 20f)).setColor(Color.YELLOW).setRenderType(renderType).renderBeam(pPoseStack.last().pose(), lastPos, pos2, 0.025f);
            lastPos = pos2;
        }
        builder.setAlpha(1f - ((float) pEntity.time / 20f)).setColor(Color.YELLOW).setRenderType(renderType).renderBeam(pPoseStack.last().pose(), lastPos, pos, 0.025f);
        pPoseStack.translate(pEntity.position().x, pEntity.position().y, pEntity.position().z);
        pPoseStack.popPose();
    }

    public Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
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
    public boolean shouldRender(ShatterAttack pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}