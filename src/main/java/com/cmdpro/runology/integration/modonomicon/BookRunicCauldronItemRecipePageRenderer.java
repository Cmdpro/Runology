package com.cmdpro.runology.integration.modonomicon;

import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.fluid.FluidHolder;
import com.klikli_dev.modonomicon.fluid.ForgeFluidHolder;
import com.klikli_dev.modonomicon.platform.ClientServices;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;

public class BookRunicCauldronItemRecipePageRenderer extends BookRecipePageRenderer<RunicCauldronItemRecipe, BookRunicCauldronItemRecipePage> {
    public BookRunicCauldronItemRecipePageRenderer(BookRunicCauldronItemRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 67;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RunicCauldronItemRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
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
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 48-4, recipeY + 24-4, 65, 27, 9, 9, 128, 256);
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 28-12, recipeY + 12-12, 77, 20, 24, 24, 128, 256);
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 28-12, recipeY + 37-12, 77, 20, 24, 24, 128, 256);
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 68-12, recipeY + 12-12, 77, 20, 24, 24, 128, 256);
        guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX + 68-12, recipeY + 37-12, 77, 20, 24, 24, 128, 256);
        this.parentScreen.renderIngredient(guiGraphics, recipeX + 28-8, recipeY + 12-8, mouseX, mouseY, recipe.getIngredients().get(0));
        renderFluidStack(guiGraphics, recipeX + 28-8, recipeY + 37-8, mouseX, mouseY, new ForgeFluidHolder(recipe.getFluidInput()), 1000);

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 68-8, recipeY + 37-8, mouseX, mouseY, recipe.getToastSymbol());
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 68-8, recipeY + 12-8, mouseX, mouseY, recipe.getResultItem(RegistryAccess.EMPTY));
    }
    public void renderFluidStack(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, FluidHolder stack, int capacity) {
        if (stack.isEmpty() || !PageRendererRegistry.isRenderable(stack)) {
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        ClientServices.FLUID.drawFluid(guiGraphics, 16, 16, stack, capacity);
        guiGraphics.pose().popPose();

        if (this.parentScreen.isMouseInRelativeRange(mouseX, mouseY, x, y, 16, 16)) {
            this.parentScreen.setTooltipStack(stack);
        }
    }
}