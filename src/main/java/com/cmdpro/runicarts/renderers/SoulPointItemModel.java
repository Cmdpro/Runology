package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.item.SoulPointItem;
import com.cmdpro.runicarts.item.SpiritTankItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulPointItemModel extends GeoModel<SoulPointItem> {
    @Override
    public ResourceLocation getModelResource(SoulPointItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/soulpoint.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulPointItem object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/soulpoint.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulPointItem animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/soulpoint.animation.json");
    }
}