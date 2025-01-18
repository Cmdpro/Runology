package com.cmdpro.runology.recipe;

import com.cmdpro.runology.registry.RecipeRegistry;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RecipeUtil {
    public static Optional<RecipeHolder<ShatterImbuementRecipe>> getShatterImbuementRecipe(Level level, int shatteredFlow, RecipeInput input) {
        return level.getRecipeManager().getRecipesFor(RecipeRegistry.SHATTER_IMBUEMENT_TYPE.get(), input, level).stream().filter((a) -> a.value().getShatteredFlowCost() <= shatteredFlow).findFirst();
    }
}
