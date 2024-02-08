package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.CastingTableItem;
import com.cmdpro.runology.item.RunicWorkbenchItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CastingTableItemRenderer extends GeoItemRenderer<CastingTableItem> {
    public CastingTableItemRenderer() {
        super(new CastingTableItemModel());
    }
    public static class CastingTableItemModel extends GeoModel<CastingTableItem> {
        @Override
        public ResourceLocation getModelResource(CastingTableItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/castingtable.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(CastingTableItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/castingtable.png");
        }

        @Override
        public ResourceLocation getAnimationResource(CastingTableItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/castingtable.animation.json");
        }
    }
}