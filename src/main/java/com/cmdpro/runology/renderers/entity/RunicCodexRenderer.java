package com.cmdpro.runology.renderers.entity;

import com.cmdpro.databank.model.DatabankEntityModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicCodex;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;


public class RunicCodexRenderer extends EntityRenderer<RunicCodex> {
    public static final ModelLayerLocation runicCodexLocation = new ModelLayerLocation(Runology.locate("runic_codex"), "main");
    Model model;
    public RunicCodexRenderer(EntityRendererProvider.Context p_272933_) {
        super(p_272933_);
        model = new Model<>(p_272933_.bakeLayer(runicCodexLocation));
    }
    @Override
    public ResourceLocation getTextureLocation(RunicCodex instance) {
        return Runology.locate("textures/item/guidebook.png");
    }

    @Override
    public void render(RunicCodex entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        model.setupAnim(entity, 0, 0, entity.tickCount+partialTick, 0, 0);
        boolean bodyVisible = !entity.isInvisible();
        boolean translucent = !bodyVisible && !entity.isInvisibleTo(Minecraft.getInstance().player);
        boolean glowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(getRenderType(entity, bodyVisible, translucent, glowing));
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
    @Nullable
    protected RenderType getRenderType(RunicCodex entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(entity);
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (bodyVisible) {
            return this.model.renderType(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    public static class Model<T extends RunicCodex> extends HierarchicalModel<T> {
        public static AnimationDefinition idle;
        private final ModelPart root;

        public Model(ModelPart pRoot) {
            this.root = pRoot.getChild("root");
        }
        public static DatabankEntityModel model;
        public static DatabankEntityModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(Runology.locate("runic_codex"));
                idle = model.animations.get("idle").createAnimationDefinition();
            }
            return model;
        }

        public static LayerDefinition createLayer() {
            return getModel().createLayerDefinition();
        }
        public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            pEntity.animState.startIfStopped(pEntity.tickCount);
            this.animate(pEntity.animState, idle, pAgeInTicks, 1.0f);
        }

        @Override
        public ModelPart root() {
            return root;
        }
    }
}
