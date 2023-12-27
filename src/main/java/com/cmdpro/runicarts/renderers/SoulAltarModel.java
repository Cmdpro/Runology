package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.entity.SoulAltarBlockEntity;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SoulAltarModel extends GeoModel<SoulAltarBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SoulAltarBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/soulaltar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulAltarBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/soulaltar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulAltarBlockEntity animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/soulaltar.animation.json");
    }
}