package com.cmdpro.runology.integration;

import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.recipe.IRunicRecipe;
import com.cmdpro.runology.recipe.ShapedRunicRecipe;
import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookRunicRecipePageRenderer extends BookRecipePageRenderer<Recipe<?>, BookRunicRecipePage> {
    public BookRunicRecipePageRenderer(BookRunicRecipePage page) {
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

        boolean shaped = recipe instanceof ShapedRunicRecipe;
        if (!shaped) {
            int iconX = recipeX + 62;
            int iconY = recipeY + 2;
            guiGraphics.blit(this.page.getBook().getCraftingTexture(), iconX, iconY, 0, 64, 11, 11, 128, 256);
            if (this.parentScreen.isMouseInRelativeRange(mouseX, mouseY, iconX, iconY, 11, 11)) {
                this.parentScreen.setTooltip(Component.translatable(ModonomiconConstants.I18n.Tooltips.RECIPE_CRAFTING_SHAPELESS));
            }
        }

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getResultItem(this.parentScreen.getMinecraft().level.registryAccess()));

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int wrap = 3;
        if (shaped) {
            wrap = ((ShapedRunicRecipe) recipe).getWidth();
        }

        for (int i = 0; i < ingredients.size(); i++) {
            this.parentScreen.renderIngredient(guiGraphics, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
        }
        guiGraphics.renderItem(new ItemStack(ItemInit.BLANKRUNE.get()), recipeX+80, recipeY+2);

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.getToastSymbol());
        if (mouseX >= recipeX+80 && recipeY+mouseY >= 2 && mouseX <= recipeX+80+16 && mouseY <= recipeY+2+16) {
            if (((IRunicRecipe)recipe).getRunicEnergyCost() != null && !((IRunicRecipe)recipe).getRunicEnergyCost().isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                for (Map.Entry<String, Float> i : ((IRunicRecipe)recipe).getRunicEnergyCost().entrySet()) {
                    tooltip.add(Component.translatable("container.runology.runicworkbench.runicenergycost", i.getValue(), Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i.getKey())))));
                }
                this.parentScreen.setTooltip(tooltip);
            }
        }
    }
}