package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.item.RunicWorkbenchItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RunicWorkbenchItemModel extends GeoModel<RunicWorkbenchItem> {
    @Override
    public ResourceLocation getModelResource(RunicWorkbenchItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/divinationtable.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunicWorkbenchItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/divinationtable.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RunicWorkbenchItem animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/divinationtable.animation.json");
    }
}