package com.cmdpro.runology.worldgen.features;

import com.cmdpro.runology.init.BlockInit;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shattercrystal extends Feature<NoneFeatureConfiguration> {
    public Shattercrystal(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        boolean reachedBottom = false;
        BlockPos pos = pContext.origin();
        if (!pContext.level().isEmptyBlock(pos)) {
            return false;
        }
        for (int i = 0; i < 64; i++) {
            if (pContext.level().getBlockState(pContext.origin().offset(0, -i, 0)).is(BlockInit.SHATTERSTONE.get())) {
                reachedBottom = true;
                pos = pContext.origin().offset(0, (-i)+1, 0);
                break;
            }
        }
        if (!reachedBottom) {
            return false;
        }
        int count = pContext.random().nextInt(8, 12);
        for (int j = 0; j < count; j++) {
            int height = pContext.random().nextInt(7, 10);
            Vec3 offset = calculateViewVector(pContext.random().nextInt(-50, -30), pContext.random().nextInt(-180, 180));
            for (int i = 0; i < height + 1; i++) {
                this.setBlock(pContext.level(), pos.offset((int) (offset.x * (float) i), (int) (offset.y * (float) i), (int) (offset.z * (float) i)), BlockInit.SHATTERCRYSTAL.get().defaultBlockState());
            }
            for (int p = 1; p < ((height - 7) / 2) + 3; p++) {
                for (int o = 0; o < 8 * p; o++) {
                    float angle2 = ((float) o) * (360f / (float) (4 * p));
                    BlockPos pos2 = pos.offset((int) (Math.sin(angle2) * (float) p), 0, (int) (Math.cos(angle2) * (float) p));
                    Vec2 angle = calculateLookAt(pos2.getCenter(), pos.offset((int) (offset.x * (float) height), (int) (offset.y * (float) height), (int) (offset.z * (float) height)).getCenter());
                    Vec3 offset2 = calculateViewVector(angle.x, angle.y);
                    for (int i = 0; i < height; i++) {
                        this.setBlock(pContext.level(), pos2.offset((int) (offset2.x * (float) i), (int) (offset2.y * (float) i), (int) (offset2.z * (float) i)), BlockInit.SHATTERCRYSTAL.get().defaultBlockState());
                    }
                }
            }
        }
        return true;
    }
    public Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }
    public Vec2 calculateLookAt(Vec3 origin, Vec3 pos) {
        double d0 = pos.x - origin.x;
        double d1 = pos.y - origin.y;
        double d2 = pos.z - origin.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return new Vec2(Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)))),
                Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
    }
}
