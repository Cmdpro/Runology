package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.RunicWorkbenchBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class RunicWorkbenchRenderer extends GeoBlockRenderer<RunicWorkbenchBlockEntity> {

    public RunicWorkbenchRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new RunicWorkbenchModel());
    }

    @Override
    public RenderType getRenderType(RunicWorkbenchBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }

    @Override
    public void postRender(PoseStack poseStack, RunicWorkbenchBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pushPose();
        poseStack.translate(0D, 1.5D, 0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getLevel().getLevelData().getGameTime() % 360));
        poseStack.scale(0.75F, 0.75F, 0.75F);
        Minecraft.getInstance().getItemRenderer().renderStatic(animatable.item, ItemDisplayContext.GUI, packedLight, packedOverlay, poseStack, bufferSource, animatable.getLevel(), 0);
        poseStack.popPose();
    }
    public static class RunicWorkbenchModel extends GeoModel<RunicWorkbenchBlockEntity> {
        @Override
        public ResourceLocation getModelResource(RunicWorkbenchBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runicworkbench.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicWorkbenchBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/runicworkbench.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicWorkbenchBlockEntity animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runicworkbench.animation.json");
        }
    }
}