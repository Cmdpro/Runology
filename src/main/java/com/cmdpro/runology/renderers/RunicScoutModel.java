package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.RunicScout;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


public class RunicScoutModel extends GeoModel<RunicScout> {
    @Override
    public ResourceLocation getModelResource(RunicScout object) {
        return new ResourceLocation(Runology.MOD_ID, "geo/runicscout.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunicScout object) {
        return new ResourceLocation(Runology.MOD_ID, "textures/entity/runicscout.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RunicScout animatable) {
        return new ResourceLocation(Runology.MOD_ID, "animations/runicscout.animation.json");
    }
}
