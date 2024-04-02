package com.cmdpro.runology.postprocessors;

import com.cmdpro.runology.Runology;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class EchoGogglesProcessor extends PostProcessor {
    @Override
    public ResourceLocation getPostChainLocation() {
        return new ResourceLocation(Runology.MOD_ID, "echogoggles");
    }

    @Override
    public void beforeProcess(PoseStack poseStack) {

    }

    @Override
    public void afterProcess() {

    }
}
