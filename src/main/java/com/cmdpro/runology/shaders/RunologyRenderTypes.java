package com.cmdpro.runology.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class RunologyRenderTypes extends RenderType {
    public RunologyRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }
    public static final ShaderStateShard SHATTER_SHADER = new ShaderStateShard(RunologyCoreShaders::getShatter);
    public static final RenderType SHATTER = create("shatter",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            CompositeState.builder().setShaderState(SHATTER_SHADER).createCompositeState(false));
}
