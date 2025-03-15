package com.cmdpro.runology.renderers.entity;

import com.cmdpro.runology.entity.RunicCodexEntry;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RunicCodexEntryRenderer extends EntityRenderer<RunicCodexEntry> {
    public RunicCodexEntryRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(RunicCodexEntry entity) {
        return null;
    }
}
