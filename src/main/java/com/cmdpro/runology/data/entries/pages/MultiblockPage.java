package com.cmdpro.runology.data.entries.pages;

import com.cmdpro.databank.multiblock.Multiblock;
import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.databank.multiblock.MultiblockRenderer;
import com.cmdpro.databank.worldgui.components.WorldGuiComponent;
import com.cmdpro.runology.api.guidebook.Page;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.MultiblockPageSerializer;
import com.cmdpro.runology.worldgui.PageWorldGui;
import com.cmdpro.runology.worldgui.components.MultiblockViewComponent;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Rotation;

import java.util.ArrayList;
import java.util.List;

public class MultiblockPage extends Page {
    public MultiblockPage(ResourceLocation multiblock) {
        this.multiblock = multiblock;
    }
    public ResourceLocation multiblock;
    @Override
    public void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        Multiblock multiblock = MultiblockManager.multiblocks.get(this.multiblock);
        pGuiGraphics.pose().pushPose();
        int sizeX = multiblock.multiblockLayers[0][0].length();
        int sizeY = multiblock.multiblockLayers.length;
        int sizeZ = multiblock.multiblockLayers[0].length;
        float maxX = 180;
        float maxY = 180;
        float diag = (float) Math.sqrt(sizeX * sizeX + sizeZ * sizeZ);
        float scaleX = maxX / diag;
        float scaleY = maxY / sizeY;
        float scale = -Math.min(scaleX, scaleY);
        pGuiGraphics.pose().translate(middleX, middleY, 100);
        pGuiGraphics.pose().scale(scale, scale, scale);
        pGuiGraphics.pose().translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
        float offX = (float) -sizeX / 2f;
        float offZ = (float) -sizeZ / 2f;
        pGuiGraphics.pose().mulPose(Axis.XP.rotationDegrees(-30));
        pGuiGraphics.pose().translate(-offX, 0, -offZ);
        pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees((((float)gui.time+Minecraft.getInstance().getTimer().getRealtimeDeltaTicks())/5f)%360f));
        pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-45));
        pGuiGraphics.pose().translate(offX, 0, offZ);
        pGuiGraphics.pose().translate(-multiblock.center.getX(), -multiblock.center.getY(), -multiblock.center.getZ());
        MultiblockRenderer.renderMultiblock(multiblock, null, pGuiGraphics.pose(), Minecraft.getInstance().getTimer(), Rotation.NONE, Minecraft.getInstance().renderBuffers().bufferSource());
        pGuiGraphics.pose().popPose();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 200);
        pGuiGraphics.pose().popPose();
    }

    @Override
    public List<WorldGuiComponent> addComponents(PageWorldGui gui, int middleX, int middleY) {
        List<WorldGuiComponent> components = new ArrayList<>(super.addComponents(gui, middleX, middleY));
        components.add(new MultiblockViewComponent(gui, middleX+50, middleY+50, multiblock));
        return components;
    }

    @Override
    public PageSerializer getSerializer() {
        return MultiblockPageSerializer.INSTANCE;
    }
}
