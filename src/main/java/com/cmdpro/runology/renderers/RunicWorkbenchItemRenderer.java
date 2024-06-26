package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.RunicWorkbenchItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicWorkbenchItemRenderer extends GeoItemRenderer<RunicWorkbenchItem> {
    public RunicWorkbenchItemRenderer() {
        super(new Model());
    }
    public static class Model extends GeoModel<RunicWorkbenchItem> {
        @Override
        public ResourceLocation getModelResource(RunicWorkbenchItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runicworkbench.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicWorkbenchItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/runicworkbench.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicWorkbenchItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runicworkbench.animation.json");
        }
    }
}