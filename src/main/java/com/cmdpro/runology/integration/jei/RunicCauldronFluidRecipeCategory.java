package com.cmdpro.runology.integration.jei;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.recipe.RunicCauldronFluidRecipe;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RunicCauldronFluidRecipeCategory implements IRecipeCategory<RunicCauldronFluidRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Runology.MOD_ID, "runiccauldronitem");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Runology.MOD_ID, "textures/gui/jeicrafting.png");

    private final IDrawable background;
    private final IDrawable icon;

    public RunicCauldronFluidRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 54, 80, 39);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.RUNICCAULDRON.get()));
    }

    @Override
    public RecipeType<RunicCauldronFluidRecipe> getRecipeType() {
        return JEIRunologyPlugin.RUNICCAULDRONFLUIDRECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.runology.runiccauldron");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RunicCauldronFluidRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 22).addFluidStack(recipe.getFluidInput().getFluid(), recipe.getFluidInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 12).addFluidStack(recipe.getFluidOutput().getFluid(), recipe.getFluidOutput().getAmount());
    }

}
