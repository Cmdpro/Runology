package com.cmdpro.runology.api.guidebook;

import com.cmdpro.databank.worldgui.components.WorldGuiComponent;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public abstract class Page {
    public abstract void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY);
    public void renderPost(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {}
    public abstract PageSerializer getSerializer();
    public boolean onClick(PageWorldGui gui, double pMouseX, double pMouseY, int pButton, int middleX, int middleY) {
        return false;
    }
    public List<WorldGuiComponent> addComponents(PageWorldGui gui, int middleX, int middleY) {
        return List.of();
    }
}
