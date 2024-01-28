package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.PurityArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.SpectralArrow;

public class PurityArrowRenderer extends ArrowRenderer<PurityArrow> {

    public PurityArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    public ResourceLocation getTextureLocation(PurityArrow pEntity) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/purityarrow.png");
    }
}