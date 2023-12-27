package com.cmdpro.runicarts.integration;
import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.init.BlockInit;
import com.cmdpro.runicarts.recipe.SoulAltarRecipe;
import com.cmdpro.runicarts.recipe.SoulShaperRecipe;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SoulAltarRecipeCategory implements IRecipeCategory<SoulAltarRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(RunicArts.MOD_ID, "soulaltar");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(RunicArts.MOD_ID, "textures/gui/jei_soulaltar.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SoulAltarRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 53, 109);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.SOULALTAR.get()));
    }

    @Override
    public RecipeType<SoulAltarRecipe> getRecipeType() {
        return JEIRunicArtsPlugin.soulAltarCategory;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.runicarts.soulaltar");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SoulAltarRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> input = recipe.getIngredients();
        ItemStack output = recipe.getResultItem(RegistryAccess.EMPTY);
        int x = 1;
        int y = 1;
        for (Ingredient i : input) {
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(i);
            x += 18;
            if (x >= (18*3)+1) {
                x = 1;
                y += 18;
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 20, 85).addItemStack(output);
    }

}
