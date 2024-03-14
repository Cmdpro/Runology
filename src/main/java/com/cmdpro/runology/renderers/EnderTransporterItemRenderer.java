package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.EnderTransporterItem;
import com.cmdpro.runology.item.SpellTableItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EnderTransporterItemRenderer extends GeoItemRenderer<EnderTransporterItem> {
    public EnderTransporterItemRenderer() {
        super(new Model());
    }
    public static class Model extends GeoModel<EnderTransporterItem> {
        @Override
        public ResourceLocation getModelResource(EnderTransporterItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/endertransporter.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(EnderTransporterItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/endertransporter.png");
        }

        @Override
        public ResourceLocation getAnimationResource(EnderTransporterItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/endertransporter.animation.json");
        }
    }
}