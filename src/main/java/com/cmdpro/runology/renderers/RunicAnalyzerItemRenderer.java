package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.RunicAnalyzerItem;
import com.cmdpro.runology.item.RunicWorkbenchItem;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ShieldItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicAnalyzerItemRenderer extends GeoItemRenderer<RunicAnalyzerItem> {
    public RunicAnalyzerItemRenderer() {
        super(new Model());
    }
    public static class Model extends GeoModel<RunicAnalyzerItem> {
        @Override
        public ResourceLocation getModelResource(RunicAnalyzerItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runicanalyzer.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicAnalyzerItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/runicanalyzer.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicAnalyzerItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runicanalyzer.animation.json");
        }
    }
}