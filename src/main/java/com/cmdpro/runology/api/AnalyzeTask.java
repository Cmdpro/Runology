package com.cmdpro.runology.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public abstract class AnalyzeTask {
    public void render(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int xOffset, int yOffset) {}
    public void renderPost(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int xOffset, int yOffset) {}
    public abstract boolean canComplete(Player player);
    public abstract AnalyzeTaskSerializer getSerializer();
    public void onComplete(Player player) {}
}
