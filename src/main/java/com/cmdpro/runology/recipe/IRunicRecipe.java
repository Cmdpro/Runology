package com.cmdpro.runology.recipe;

import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.RecipeInit;
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
        return true;
    }

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.RUNICWORKBENCH.get());
    }

    @Override
    default CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }
}
