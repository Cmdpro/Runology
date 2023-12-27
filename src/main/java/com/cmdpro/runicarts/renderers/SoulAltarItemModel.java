package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.item.SoulAltarItem;
import com.cmdpro.runicarts.item.SoulPointItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulAltarItemModel extends GeoModel<SoulAltarItem> {
    @Override
    public ResourceLocation getModelResource(SoulAltarItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/soulaltar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulAltarItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/soulaltar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulAltarItem animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/soulaltar.animation.json");
    }
}