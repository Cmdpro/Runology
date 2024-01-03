package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.RunicWorkbenchBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RunicWorkbenchModel extends GeoModel<RunicWorkbenchBlockEntity> {
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