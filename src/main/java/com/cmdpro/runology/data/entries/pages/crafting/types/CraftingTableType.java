package com.cmdpro.runology.data.entries.pages.crafting.types;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.guidebook.CraftingType;
import com.cmdpro.runology.data.entries.pages.CraftingPage;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

public class CraftingTableType extends CraftingType {
    public static final ResourceLocation GUIDEBOOK_CRAFTING = Runology.locate("textures/gui/guidebook_crafting.png");
    @Override
    public void render(CraftingPage page, PageWorldGui gui, GuiGraphics pGuiGraphics, int middleX, int x, int middleY, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof CraftingRecipe recipe2) {
            int recipeX = middleX + x - (98/2);
            int recipeY = middleY + y - (52/2);
            pGuiGraphics.blit(GUIDEBOOK_CRAFTING, recipeX, recipeY, 0, 0, 98, 52);
            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), recipeX + 81, recipeY + 18, pMouseX, pMouseY);
            if (recipe2 instanceof ShapelessRecipe) {
                pGuiGraphics.blit(GUIDEBOOK_CRAFTING, recipeX + 62, recipeY + 35, 0, 54, 8, 6);
            }
            int x2 = 1;
            int y2 = 1;
            int p = 0;
            int wrap = 3;
            if (recipe2 instanceof ShapedRecipe shaped) {
                wrap = shaped.getWidth();
            }
            for (Ingredient o : recipe2.getIngredients()) {
                page.renderIngredientWithTooltip(gui, pGuiGraphics, o, recipeX + x2, recipeY + y2, pMouseX, pMouseY);
                x2 += 17;
                p++;
                if (p >= wrap) {
                    p = 0;
                    x2 = 1;
                    y2 += 17;
                }
            }
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeType.CRAFTING);
    }
}
