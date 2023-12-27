package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SpiritTankModel extends GeoModel<SpiritTankBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SpiritTankBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "geo/spirittank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpiritTankBlockEntity object) {
        return new ResourceLocation(RunicArts.MOD_ID, "textures/block/spirittank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpiritTankBlockEntity animatable) {
        return new ResourceLocation(RunicArts.MOD_ID, "animations/spirittank.animation.json");
    }

    @Override
    public void setCustomAnimations(SpiritTankBlockEntity animatable, long instanceId, AnimationState<SpiritTankBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone fill = getAnimationProcessor().getBone("fill");

        if (fill != null) {
            if (animatable.getSouls() <= 0) {
                fill.setPosY(-1);
                fill.setScaleY(0);
            } else {
                fill.setPosY(0);
                fill.setScaleY(animatable.getSouls()/animatable.getMaxSouls());
            }
        }
    }
}