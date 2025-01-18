package com.cmdpro.runology.integration.emi.recipe;

import com.cmdpro.runology.integration.emi.EmiRunologyPlugin;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EmiShatterImbuementRecipe  implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient input;
    private final EmiStack output;
    private final int shatteredFlow;

    public EmiShatterImbuementRecipe(ResourceLocation id, ShatterInfusionRecipe recipe) {
        this.id = id;
        this.input = EmiIngredient.of(recipe.getIngredients().get(0));
        this.output = EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
        shatteredFlow = recipe.getShatteredFlowCost();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiRunologyPlugin.SHATTER_INFUSION;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 112;
    }

    @Override
    public int getDisplayHeight() {
        return 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 44, 1);
        widgets.addSlot(input, 22, 0);
        widgets.addSlot(output, 72, 0).recipeContext(this);
        Component text = Component.translatable("emi.category.runology.shatter_infusion.cost", shatteredFlow);
        widgets.addText(text, 56-(Minecraft.getInstance().font.width(text)/2), 22, 0xFFFFFFFF, true);
    }
}
