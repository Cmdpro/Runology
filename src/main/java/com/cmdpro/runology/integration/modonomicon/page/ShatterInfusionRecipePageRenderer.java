package com.cmdpro.runology.integration.modonomicon.page;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import com.klikli_dev.modonomicon.book.page.BookProcessingRecipePage;
import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class ShatterInfusionRecipePageRenderer extends BookRecipePageRenderer<ShatterInfusionRecipe, ShatterInfusionRecipePage> {
    public ShatterInfusionRecipePageRenderer(ShatterInfusionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 59;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<ShatterInfusionRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {

        recipeY += 10;

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookEntryScreen.PAGE_WIDTH / 2, 0);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookEntryScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
            }
        }

        RenderSystem.enableBlend();
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX, recipeY, 11, 71, 96, 24, 128, 256);

        this.parentScreen.renderIngredient(guiGraphics, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.value().getIngredients().get(0));
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 40, recipeY + 4, mouseX, mouseY, recipe.value().getToastSymbol());
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()));
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("emi.category.runology.shatter_infusion.cost", recipe.value().getShatteredFlowCost()), BookEntryScreen.PAGE_WIDTH / 2, recipeY+30, 0xFFFFFFFF);
    }
}
