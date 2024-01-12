package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


public class RunicConstructModel extends GeoModel<RunicConstruct> {
    @Override
    public ResourceLocation getModelResource(RunicConstruct object) {
        return new ResourceLocation(Runology.MOD_ID, "geo/runicconstruct.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunicConstruct object) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicconstruct.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RunicConstruct animatable) {
        return new ResourceLocation(Runology.MOD_ID, "animations/runicconstruct.animation.json");
    }
}
