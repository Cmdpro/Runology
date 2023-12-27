package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.entity.SoulPointBlockEntity;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SoulPointModel extends GeoModel<SoulPointBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SoulPointBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/soulpoint.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulPointBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/soulpoint.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulPointBlockEntity animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/soulpoint.animation.json");
    }
}