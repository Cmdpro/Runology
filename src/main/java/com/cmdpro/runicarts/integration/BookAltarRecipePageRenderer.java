package com.cmdpro.runicarts.integration;

import com.klikli_dev.modonomicon.api.ModonomiconConstants.I18n.Tooltips;
import com.klikli_dev.modonomicon.book.page.BookCraftingRecipePage;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class BookAltarRecipePageRenderer extends BookRecipePageRenderer<Recipe<?>, BookAltarRecipePage> {
    public BookAltarRecipePageRenderer(BookAltarRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 78;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, Recipe<?> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookContentScreen.PAGE_WIDTH / 2, -5);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookContentScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
            }
        }

        RenderSystem.enableBlend();
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX - 2, recipeY - 2, 0, 0, 100, 62, 128, 256);

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int wrap = 3;

        for (int i = 0; i < ingredients.size(); i++) {
            this.parentScreen.renderIngredient(guiGraphics, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
        }

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.getToastSymbol());
    }
}