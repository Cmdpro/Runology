package com.cmdpro.runology.api.guidebook;

import com.cmdpro.runology.data.entries.pages.CraftingPage;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Recipe;

public abstract class CraftingType {
    public abstract void render(CraftingPage page, PageWorldGui gui, GuiGraphics pGuiGraphics, int xOffset, int x, int yOffset, int y, Recipe recipe, int pMouseX, int pMouseY);
    public abstract boolean isRecipeType(Recipe recipe);
}
