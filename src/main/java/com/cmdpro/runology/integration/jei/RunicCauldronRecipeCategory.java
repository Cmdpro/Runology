package com.cmdpro.runology.integration.jei;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.recipe.IRunicCauldronRecipe;
import com.cmdpro.runology.recipe.RunicCauldronFluidRecipe;
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

public class RunicCauldronRecipeCategory implements IRecipeCategory<IRunicCauldronRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Runology.MOD_ID, "runiccauldron");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Runology.MOD_ID, "textures/gui/jeicrafting.png");

    private final IDrawable background;
    private final IDrawable icon;

    public RunicCauldronRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 54, 80, 39);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.RUNICCAULDRON.get()));
    }

    @Override
    public RecipeType<IRunicCauldronRecipe> getRecipeType() {
        return JEIRunologyPlugin.RUNICCAULDRONRECIPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, IRunicCauldronRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 22).addFluidStack(recipe.getFluidInput().getFluid(), recipe.getFluidInput().getAmount());
        if (recipe.isFluidOutput()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 12).addFluidStack(recipe.getFluidOutput().getFluid(), recipe.getFluidOutput().getAmount());
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 12).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
        }
    }

}
