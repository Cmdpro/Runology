package com.cmdpro.runology.renderers.entity;

import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.entity.DatabankEntityModel;
import com.cmdpro.databank.model.entity.DatabankEntityRenderer;
import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.databank.rendering.RenderProjectionUtil;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.data.entries.EntryTab;
import com.cmdpro.runology.entity.RunicCodex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;


public class RunicCodexRenderer extends DatabankEntityRenderer<RunicCodex> {
    public static final ResourceLocation GUIDEBOOK_MISC = Runology.locate("textures/gui/guidebook_misc.png");
    public RunicCodexRenderer(EntityRendererProvider.Context context) {
        super(context, new Model(), 0.5F);
    }
    @Override
    public ResourceLocation getTextureLocation(RunicCodex instance) {
        return Runology.locate("textures/item/guidebook.png");
    }

    @Override
    public void render(RunicCodex pEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(pEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        MultiBufferSource.BufferSource buffer = RenderHandler.createBufferSource();
        if (pEntity.tab != null && !pEntity.entryOpen) {
            EntryTab tab = pEntity.getTab();
            Component name = tab.name.copy().withStyle(ChatFormatting.BOLD);
            int width = (Math.max(20, Minecraft.getInstance().font.width(name)) + 8);
            int height = (24 + Minecraft.getInstance().font.lineHeight + 8);
            float worldWidth = width / 128f;
            float worldHeight = height / 128f;
            Vec3 center = pEntity.getBoundingBox().getCenter();
            RenderProjectionUtil.project((graphics) -> {
                graphics.blitWithBorder(GUIDEBOOK_MISC, 0, 0, 0, 0, width, height, 10, 10, 2);
                float itemScale = 2;
                graphics.pose().pushPose();
                graphics.pose().scale(itemScale, itemScale, itemScale);
                graphics.renderItem(tab.icon, (int)(((width/2f)-(8f*itemScale))/itemScale), 0);
                graphics.pose().popPose();
                graphics.drawCenteredString(Minecraft.getInstance().font, name, width/2, 28, 0xFFFFFFFF);
            }, (stack) -> {
                stack.pushPose();
                stack.translate(center.x, center.y, center.z);
                stack.pushPose();
                Vec2 angle = angleToClient(pEntity);
                stack.translate(0, 0.75, 0);
                stack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-angle.y + 180)));
                stack.translate(0, 0, -0.25);
                stack.pushPose();
            }, (stack) -> {
                stack.popPose();
                stack.popPose();
                stack.popPose();
            }, buffer, poseStack, new Vec3(-worldWidth / 2f, worldHeight / 2f, 0), new Vec3(worldWidth / 2f, worldHeight / 2f, 0), new Vec3(worldWidth / 2f, -worldHeight / 2f, 0), new Vec3(-worldWidth / 2f, -worldHeight / 2f, 0), width, height, 3f);
        }
    }
    public static Vec2 angleToClient(RunicCodex entity) {
        Vec3 pointA = entity.position();
        Vec3 pointB = Minecraft.getInstance().player.getEyePosition();
        return calculateRotationVector(pointA, pointB);
    }
    private static Vec2 calculateRotationVector(Vec3 pVec, Vec3 pTarget) {
        double d0 = pTarget.x - pVec.x;
        double d1 = pTarget.y - pVec.y;
        double d2 = pTarget.z - pVec.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return new Vec2(
                Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)))),
                Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F)
        );
    }

    public static class Model extends DatabankEntityModel<RunicCodex> {
        public DatabankModel model;
        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(Runology.locate("runic_codex"));
            }
            return model;
        }

        @Override
        public ResourceLocation getTextureLocation() {
            return Runology.locate("textures/item/guidebook.png");
        }
        @Override
        public void setupModelPose(RunicCodex pEntity, float partialTick) {
            pEntity.animState.updateAnimDefinitions(getModel());
            animate(pEntity.animState);
            modelPose.stringToPart.get("root").pos.y += (float)Math.sin(Math.toRadians((pEntity.animState.getTime() % 1f)*360))-0.5f;
        }
    }
}
