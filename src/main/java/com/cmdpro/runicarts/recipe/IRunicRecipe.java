package com.cmdpro.runicarts.recipe;

import com.cmdpro.runicarts.init.BlockInit;
import com.cmdpro.runicarts.init.RecipeInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRunicRecipe extends CraftingRecipe, IHasRequiredKnowledge, IHasRunicEnergyCost {
    @Override
    default RecipeType<?> getType() {
        return RecipeInit.RUNICCRAFTING.get();
    }

    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.RUNICWORKBENCH.get());
    }

    @Override
    default CraftingBookCategory category() {
        // Arcane recipes use a separate recipe book, so an accurate crafting book category isn't needed
        return CraftingBookCategory.MISC;
    }
}
