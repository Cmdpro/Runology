package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.item.SpellTableItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpellTableItemRenderer extends GeoItemRenderer<SpellTableItem> {
    public SpellTableItemRenderer() {
        super(new SpellTableItemModel());
    }
    public static class SpellTableItemModel extends GeoModel<SpellTableItem> {
        @Override
        public ResourceLocation getModelResource(SpellTableItem object) {
            return new ResourceLocation(Runology.MOD_ID, "geo/spelltable.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(SpellTableItem object) {
            return new ResourceLocation(Runology.MOD_ID, "textures/block/spelltable.png");
        }

        @Override
        public ResourceLocation getAnimationResource(SpellTableItem animatable) {
            return new ResourceLocation(Runology.MOD_ID, "animations/spelltable.animation.json");
        }
    }
}