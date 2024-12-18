package com.cmdpro.runology.shaders;

import com.cmdpro.databank.Databank;
import com.cmdpro.databank.rendering.RenderTypeHandler;
import com.cmdpro.runology.Runology;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import static com.cmdpro.databank.rendering.RenderTypeHandler.PARTICLE_SHEET;
import static com.cmdpro.databank.rendering.RenderTypeHandler.TRANSPARENCY;

public class RunologyRenderTypes extends RenderType {
    public RunologyRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }
    public static final ShaderStateShard SHATTER_SHADER = new ShaderStateShard(RunologyCoreShaders::getShatter);
    public static final ShaderStateShard SHATTER_PARTICLE_SHADER = new ShaderStateShard(RunologyCoreShaders::getShatterParticle);
    public static final RenderType SHATTER = create(Runology.MODID + ":shatter",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            CompositeState.builder().setShaderState(SHATTER_SHADER).createCompositeState(false));
    public static final RenderType SHATTER_PARTICLE = RenderTypeHandler.registerRenderType(create(Runology.MODID + ":shatter_particle",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder().setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).setLightmapState(RenderStateShard.LIGHTMAP).setTransparencyState(TRANSPARENCY).setTextureState(PARTICLE_SHEET).setShaderState(SHATTER_PARTICLE_SHADER).createCompositeState(false)), true);
}
