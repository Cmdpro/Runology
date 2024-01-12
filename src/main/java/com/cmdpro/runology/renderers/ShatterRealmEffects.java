package com.cmdpro.runology.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class ShatterRealmEffects extends DimensionSpecialEffects {

    public ShatterRealmEffects() {
        super(Float.NaN, true, SkyType.NONE, false, false);
    }

    public Vec3 getBrightnessDependentFogColor(Vec3 p_108894_, float p_108895_) {
        return p_108894_.scale((double)0.15F);
    }

    public boolean isFoggyAt(int p_108891_, int p_108892_) {
        return true;
    }

    @Nullable
    public float[] getSunriseColor(float p_108888_, float p_108889_) {
        return null;
    }
}