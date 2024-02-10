package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.IceShard;
import com.cmdpro.runology.entity.PurityArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class IceShardRenderer extends ArrowRenderer<IceShard> {

    public IceShardRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public ResourceLocation getTextureLocation(IceShard pEntity) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/iceshard.png");
    }
}