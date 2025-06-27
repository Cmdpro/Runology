package com.cmdpro.runology.data.entries.pages;

import com.cmdpro.runology.api.guidebook.CraftingType;
import com.cmdpro.runology.api.guidebook.CraftingTypes;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.CraftingPageSerializer;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CraftingPage extends TextPage {
    public CraftingPage(Component text, List<ResourceLocation> recipes) {
        super(text);
        this.recipes = recipes;
    }
    public List<ResourceLocation> recipes;
    @Override
    public int textYOffset() {
        int y = getYStart();
        for (ResourceLocation i : recipes) {
            int shift = 0;
            Optional<? extends RecipeHolder<?>> optional = Minecraft.getInstance().level.getRecipeManager().byKey(i);
            if (optional.isPresent()) {
                for (CraftingType o : CraftingTypes.types) {
                    if (o.isRecipeType(optional.get().value())) {
                        shift = o.getYHeight();
                    }
                }
            }
            y += shift+10;
        }
        return y;
    }
    public List<FormattedCharSequence> tooltipToShow = new ArrayList<>();
    public boolean showTooltip;
    @Override
    public void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        super.render(gui, pGuiGraphics, pPartialTick, pMouseX, pMouseY, middleX, middleY);
        int x = 4;
        int y = getYStart();
        for (ResourceLocation i : recipes) {
            int shift = 0;
            Optional<? extends RecipeHolder<?>> optional = Minecraft.getInstance().level.getRecipeManager().byKey(i);
            if (optional.isPresent()) {
                for (CraftingType o : CraftingTypes.types) {
                    if (o.isRecipeType(optional.get().value())) {
                        shift = o.getYHeight();
                        o.render(this, gui, pGuiGraphics, middleX, x, middleY, y, optional.get().value(), pMouseX, pMouseY);
                        break;
                    }
                }
            }
            y += shift+10;
        }
    }
    public int getYStart() {
        int y = 4;
        for (ResourceLocation i : recipes) {
            int shift = 0;
            Optional<? extends RecipeHolder<?>> optional = Minecraft.getInstance().level.getRecipeManager().byKey(i);
            if (optional.isPresent()) {
                for (CraftingType o : CraftingTypes.types) {
                    if (o.isRecipeType(optional.get().value())) {
                        shift = o.getYHeight();
                        break;
                    }
                }
            }
            y -= (shift + 10)/2;
        }
        return y;
    }
    @Override
    public void renderPost(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        super.renderPost(gui, pGuiGraphics, pPartialTick, pMouseX, pMouseY, middleX, middleY);
        if (showTooltip) {
            showTooltip = false;
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipToShow, pMouseX, pMouseY);
        }
    }
    public void renderItemWithTooltip(GuiGraphics graphics, ItemStack item, int x, int y, int mouseX, int mouseY) {
        graphics.renderItem(item, x, y);
        graphics.renderItemDecorations(Minecraft.getInstance().font, item, x, y);
        if (mouseX >= x && mouseY >= y) {
            if (mouseX <= x + 16 && mouseY <= y + 16) {
                showTooltip = true;
                tooltipToShow.clear();
                for (Component i : Screen.getTooltipFromItem(Minecraft.getInstance(), item)) {
                    tooltipToShow.add(i.getVisualOrderText());
                }
            }
        }
    }
    public void renderIngredientWithTooltip(PageWorldGui gui, GuiGraphics graphics, Ingredient item, int x, int y, int mouseX, int mouseY) {
        if (!item.isEmpty()) {
            ItemStack currentItem = item.getItems()[(gui.time / 20) % item.getItems().length];
            graphics.renderItem(currentItem, x, y);
            if (mouseX >= x && mouseY >= y) {
                if (mouseX <= x + 16 && mouseY <= y + 16) {
                    showTooltip = true;
                    tooltipToShow.clear();
                    for (Component i : Screen.getTooltipFromItem(Minecraft.getInstance(), currentItem)) {
                        tooltipToShow.add(i.getVisualOrderText());
                    }
                }
            }
        }
    }
    public void renderFluidIngredientWithTooltip(PageWorldGui gui, GuiGraphics graphics, FluidIngredient fluid, int x, int y, int mouseX, int mouseY) {
        if (!fluid.isEmpty()) {
            FluidStack currentFluid = fluid.getStacks()[(gui.time / 20) % fluid.getStacks().length];
            renderFluidWithTooltip(graphics, currentFluid, x, y, mouseX, mouseY);
        }
    }
    public void renderFluidWithTooltip(GuiGraphics graphics, FluidStack fluid, int x, int y, int mouseX, int mouseY) {
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluid);
        if (stillTexture != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(fluid);
            Color tint = new Color(tintColor);
            graphics.blit(x, y, 0, 16, 16, sprite, (float)tint.getRed()/255f, (float)tint.getGreen()/255f, (float)tint.getBlue()/255f, (float)tint.getAlpha()/255f);
        }
        if (mouseX <= x+16 && mouseY <= y+16 && mouseX >= x && mouseY >= y) {
            if (!fluid.isEmpty()) {
                showTooltip = true;
                tooltipToShow.clear();
                tooltipToShow.add(Component.translatable("gui.widget.fluid.without_max", fluid.getAmount(), fluid.getHoverName()).getVisualOrderText());
            } else {
                showTooltip = true;
                tooltipToShow.clear();
                tooltipToShow.add(Component.empty().getVisualOrderText());
            }
        }
    }
    @Override
    public PageSerializer getSerializer() {
        return CraftingPageSerializer.INSTANCE;
    }
}
