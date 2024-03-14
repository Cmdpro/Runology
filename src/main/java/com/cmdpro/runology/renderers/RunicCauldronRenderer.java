package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.RunicCauldronBlockEntity;
import com.cmdpro.runology.block.entity.SpellTableBlockEntity;
import com.cmdpro.runology.block.entity.VoidGlassBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class RunicCauldronRenderer extends GeoBlockRenderer<RunicCauldronBlockEntity> {

    public RunicCauldronRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public RenderType getRenderType(RunicCauldronBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    @Override
    public void postRender(PoseStack poseStack, RunicCauldronBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (!animatable.isFluidEmpty()) {
            float height = getFluidHeight();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(animatable.getFluid().getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(animatable.getFluid());
            if (stillTexture != null) {
                TextureAtlasSprite sprite =
                        Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
                int tintColor = fluidTypeExtensions.getTintColor(animatable.getFluid());
                VertexConsumer builder = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(animatable.getFluid().getFluid().defaultFluidState()));
                drawQuad(builder, poseStack, (1f/8f)-0.5f, (height/16f)+0.25f, (1f/8f)-0.5f, 0.75f+(1f/8f)-0.5f, (height/16f)+0.25f, 0.75f+(1f/8f)-0.5f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, tintColor);
            }
        }
    }
    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.vertex(poseStack.last().pose(), x, y, z)
                .color(color)
                .uv(u, v)
                .uv2(packedLight)
                .normal(1, 0, 0)
                .endVertex();
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
    }
    public float getFluidHeight() {
        return 12f*(((float)animatable.getFluidAmount())/((float)animatable.getCapacity()));
    }
    public static class Model extends GeoModel<RunicCauldronBlockEntity> {
        @Override
        public ResourceLocation getModelResource(RunicCauldronBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runiccauldron.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicCauldronBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/runiccauldron.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicCauldronBlockEntity animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runiccauldron.animation.json");
        }
    }
}