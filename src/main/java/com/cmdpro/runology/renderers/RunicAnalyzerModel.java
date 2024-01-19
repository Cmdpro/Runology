package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.RunicAnalyzerBlockEntity;
import com.cmdpro.runology.block.entity.RunicWorkbenchBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RunicAnalyzerModel extends GeoModel<RunicAnalyzerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RunicAnalyzerBlockEntity object) {
        return new ResourceLocation(Runology.MOD_ID, "geo/runicanalyzer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunicAnalyzerBlockEntity object) {
        return new ResourceLocation(Runology.MOD_ID, "textures/block/runicanalyzer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RunicAnalyzerBlockEntity animatable) {
        return new ResourceLocation(Runology.MOD_ID, "animations/runicanalyzer.animation.json");
    }
}