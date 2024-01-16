package com.cmdpro.runology.integration;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.recipe.IRunicRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunicCraftingRecipeCategory implements IRecipeCategory<IRunicRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Runology.MOD_ID, "runiccrafting");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Runology.MOD_ID, "textures/gui/runiccraftingjei.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final ICraftingGridHelper craftingGridHelper;

    public RunicCraftingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.RUNICWORKBENCHITEM.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IRunicRecipe recipe, IFocusGroup focuses) {
        // Initialize recipe output
        this.craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, List.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));

        // Initialize recipe inputs
        int width = (recipe instanceof IShapedRecipe<?> shapedRecipe) ? shapedRecipe.getRecipeWidth() : 0;
        int height = (recipe instanceof IShapedRecipe<?> shapedRecipe) ? shapedRecipe.getRecipeHeight() : 0;
        List<List<ItemStack>> inputs = recipe.getIngredients().stream().map(ingredient -> List.of(ingredient.getItems())).toList();
        this.craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, width, height);
    }

    @Override
    public void draw(IRunicRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.getRunicEnergyCost() != null && !recipe.getRunicEnergyCost().isEmpty()) {
            guiGraphics.renderItem(new ItemStack(ItemInit.BLANKRUNE.get()), 64, 0);
        }
    }

    @Override
    public List<Component> getTooltipStrings(IRunicRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = new ArrayList<>();
        if (recipe.getRunicEnergyCost() != null && !recipe.getRunicEnergyCost().isEmpty() && mouseX >= 64 && mouseX <= 64+16 && mouseY >= 0 && mouseY <= 16) {
            for (Map.Entry<String, Float> i : recipe.getRunicEnergyCost().entrySet()) {
                tooltip.add(Component.translatable("container.runology.runicworkbench.runicenergycost", i.getValue(), Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i.getKey())))));
            }
        }
        return tooltip;
    }

    @Override
    public RecipeType<IRunicRecipe> getRecipeType() {
        return JEIRunologyPlugin.RUNICRECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.runology.runicworkbench");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
}