package com.cmdpro.runology.integration;

import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.recipe.IRunicRecipe;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import com.cmdpro.runology.recipe.ShapedRunicRecipe;
import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.modonomicon.fluid.ForgeFluidHolder;
import com.klikli_dev.modonomicon.platform.services.FluidHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookRunicCauldronItemRecipePageRenderer extends BookRecipePageRenderer<Recipe<?>, BookRunicCauldronItemRecipePage> {
    public BookRunicCauldronItemRecipePageRenderer(BookRunicCauldronItemRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 78;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, Recipe<?> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        if (recipe instanceof RunicCauldronItemRecipe recipe2) {
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
            guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 44, recipeY + 24, 65, 27, 9, 9, 128, 256);

            this.parentScreen.renderIngredient(guiGraphics, recipeX + 28, recipeY + 14, mouseX, mouseY, recipe.getIngredients().get(0));
            this.parentScreen.renderFluidStack(guiGraphics, recipeX + 28, recipeY + 38, mouseX, mouseY, new ForgeFluidHolder(recipe2.getFluidInput()), FluidType.BUCKET_VOLUME);


            this.parentScreen.renderItemStack(guiGraphics, recipeX + 68, recipeY + 24, mouseX, mouseY, recipe.getToastSymbol());
        }
    }
}