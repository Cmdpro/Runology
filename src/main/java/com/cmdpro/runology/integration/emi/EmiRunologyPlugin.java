package com.cmdpro.runology.integration.emi;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.integration.emi.recipe.EmiShatterImbuementRecipe;
import com.cmdpro.runology.recipe.ShatterImbuementRecipe;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.RecipeRegistry;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class EmiRunologyPlugin implements EmiPlugin {
    public static final ResourceLocation EMI_ICONS = ResourceLocation.fromNamespaceAndPath(Runology.MODID,"textures/gui/emi_icons.png");

    public static final EmiStack SHATTER_WORKSTATION = EmiStack.of(BlockRegistry.SHATTER.get());
    public static final EmiStack SHATTERED_INFUSER_WORKSTATION = EmiStack.of(BlockRegistry.SHATTERED_INFUSER.get());

    public static final EmiRecipeCategory SHATTER_IMBUEMENT = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Runology.MODID,"shatter_imbuement"), SHATTER_WORKSTATION, new EmiTexture(EMI_ICONS, 0, 0, 16, 16));

    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(SHATTER_IMBUEMENT);

        emiRegistry.addWorkstation(SHATTER_IMBUEMENT, SHATTER_WORKSTATION);
        emiRegistry.addWorkstation(SHATTER_IMBUEMENT, SHATTERED_INFUSER_WORKSTATION);

        RecipeManager manager = emiRegistry.getRecipeManager();

        for (RecipeHolder<ShatterImbuementRecipe> recipe : manager.getAllRecipesFor(RecipeRegistry.SHATTER_IMBUEMENT_TYPE.get())) {
            emiRegistry.addRecipe(new EmiShatterImbuementRecipe(recipe.id(), recipe.value()));
        }
    }
}