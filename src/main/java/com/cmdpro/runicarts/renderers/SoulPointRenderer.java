package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.entity.SoulPointBlockEntity;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;

import java.util.List;


public class SoulPointRenderer extends GeoBlockRenderer<SoulPointBlockEntity> {

    public SoulPointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new SoulPointModel());
    }
}