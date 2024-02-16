package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.SpellTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class SpellTableRenderer extends GeoBlockRenderer<SpellTableBlockEntity> {

    public SpellTableRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new SpellTableModel());
    }

    @Override
    public RenderType getRenderType(SpellTableBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class SpellTableModel extends GeoModel<SpellTableBlockEntity> {
        @Override
        public ResourceLocation getModelResource(SpellTableBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/spelltable.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(SpellTableBlockEntity object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/spelltable.png");
        }

        @Override
        public ResourceLocation getAnimationResource(SpellTableBlockEntity animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/spelltable.animation.json");
        }
    }
}