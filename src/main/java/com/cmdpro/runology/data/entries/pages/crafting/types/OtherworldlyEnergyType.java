package com.cmdpro.runology.data.entries.pages.crafting.types;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.guidebook.CraftingType;
import com.cmdpro.runology.data.entries.pages.CraftingPage;
import com.cmdpro.runology.recipe.OtherworldlyEnergyRecipe;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.RecipeRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class OtherworldlyEnergyType extends CraftingType {
    public static final ResourceLocation GUIDEBOOK_CRAFTING = Runology.locate("textures/gui/guidebook_crafting.png");
    @Override
    public void render(CraftingPage page, PageWorldGui gui, GuiGraphics pGuiGraphics, int middleX, int x, int middleY, int y, Recipe recipe, int pMouseX, int pMouseY) {
        if (recipe instanceof OtherworldlyEnergyRecipe recipe2) {
            int recipeX = middleX + x - (63/2);
            int recipeY = middleY + y;
            pGuiGraphics.blit(GUIDEBOOK_CRAFTING, recipeX, recipeY+16, 98, 0, 64, 18);
            page.renderItemWithTooltip(pGuiGraphics, new ItemStack(BlockRegistry.OTHERWORLDLY_ENERGY.get()), recipeX + 24, recipeY, pMouseX, pMouseY);
            page.renderIngredientWithTooltip(gui, pGuiGraphics, recipe2.getIngredients().getFirst(), recipeX + 1, recipeY + 17, pMouseX, pMouseY);
            page.renderItemWithTooltip(pGuiGraphics, recipe.getResultItem(RegistryAccess.EMPTY), recipeX + 47, recipeY + 17, pMouseX, pMouseY);
        }
    }

    @Override
    public boolean isRecipeType(Recipe recipe) {
        return recipe.getType().equals(RecipeRegistry.OTHERWORLDLY_ENERGY_TYPE.get());
    }

    @Override
    public int getYHeight() {
        return 36;
    }
}
