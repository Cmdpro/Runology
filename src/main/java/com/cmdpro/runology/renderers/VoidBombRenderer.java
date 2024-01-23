package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicOverseer;
import com.cmdpro.runology.entity.VoidBomb;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class VoidBombRenderer extends GeoEntityRenderer<VoidBomb> {
    public VoidBombRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VoidBombModel());
        this.shadowRadius = 0.5f;
    }
    @Override
    public ResourceLocation getTextureLocation(VoidBomb instance) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/voidbomb.png");
    }

    @Override
    public RenderType getRenderType(VoidBomb animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
    public static class VoidBombModel extends GeoModel<VoidBomb> {
        @Override
        public ResourceLocation getModelResource(VoidBomb object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/voidbomb.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(VoidBomb object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/entity/voidbomb.png");
        }

        @Override
        public ResourceLocation getAnimationResource(VoidBomb animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/voidbomb.animation.json");
        }
    }
}
