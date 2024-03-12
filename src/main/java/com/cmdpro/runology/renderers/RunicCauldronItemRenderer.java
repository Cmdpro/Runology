package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.RunicCauldronItem;
import com.cmdpro.runology.item.SpellTableItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicCauldronItemRenderer extends GeoItemRenderer<RunicCauldronItem> {
    public RunicCauldronItemRenderer() {
        super(new RunicCauldronItemModel());
    }
    public static class RunicCauldronItemModel extends GeoModel<RunicCauldronItem> {
        @Override
        public ResourceLocation getModelResource(RunicCauldronItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/runiccauldron.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RunicCauldronItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/runiccauldron.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RunicCauldronItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/runiccauldron.animation.json");
        }
    }
}