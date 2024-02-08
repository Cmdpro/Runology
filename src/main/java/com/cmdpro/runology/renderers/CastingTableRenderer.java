package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.CastingTableBlockEntity;
import com.cmdpro.runology.block.entity.RunicWorkbenchBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;


public class CastingTableRenderer extends GeoBlockRenderer<CastingTableBlockEntity> {

    public CastingTableRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new CastingTableModel());
    }

    @Override
    public RenderType getRenderType(CastingTableBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class CastingTableModel extends GeoModel<CastingTableBlockEntity> {
        @Override
        public ResourceLocation getModelResource(CastingTableBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/castingtable.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(CastingTableBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/castingtable.png");
        }

        @Override
        public ResourceLocation getAnimationResource(CastingTableBlockEntity animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/castingtable.animation.json");
        }
    }
}