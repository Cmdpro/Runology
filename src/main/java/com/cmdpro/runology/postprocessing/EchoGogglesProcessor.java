package com.cmdpro.runology.postprocessing;

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
    public void beforeProcess(PoseStack viewModelStack) {

    }

    @Override
    public void afterProcess() {

    }
}
