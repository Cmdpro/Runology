package com.cmdpro.runology.recipe;

import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.RecipeInit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public interface IRunicCauldronRecipe extends Recipe<SimpleContainer> {
    @Override
    default RecipeType<?> getType() {
        return RecipeInit.RUNICCRAFTING.get();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }

    ItemStack TOAST_SYMBOL = new ItemStack(ItemInit.RUNICCAULDRONITEM.get());
    @Override
    default ItemStack getToastSymbol() {
        return TOAST_SYMBOL;
    }

    boolean isFluidOutput();
    default FluidStack getFluidOutput() {
        return null;
    }
    FluidStack getFluidInput();
}
