package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.item.DivinationTableItem;
import com.cmdpro.runicarts.item.SoulAltarItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DivinationTableItemModel extends GeoModel<DivinationTableItem> {
    @Override
    public ResourceLocation getModelResource(DivinationTableItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/divinationtable.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DivinationTableItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/divinationtable.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DivinationTableItem animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/divinationtable.animation.json");
    }
}