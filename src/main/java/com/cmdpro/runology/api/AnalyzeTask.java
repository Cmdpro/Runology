package com.cmdpro.runology.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public abstract class AnalyzeTask {
    public abstract void render(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY);
    public abstract void renderPost(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY);
    public abstract boolean canComplete(Player player);
    public abstract AnalyzeTaskSerializer getSerializer();
}
