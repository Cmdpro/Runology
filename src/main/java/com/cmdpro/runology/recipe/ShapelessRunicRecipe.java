package com.cmdpro.runology.recipe;


import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.RecipeInit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ShapelessRunicRecipe implements IRunicRecipe {
    private final ResourceLocation id;
    private final ShapelessRecipe recipe;
    private final String entry;
    private final Map<String, Float> runicEnergy;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public ShapelessRunicRecipe(ShapelessRecipe recipe, String entry, Map<String, Float> runicEnergy) {
        this.id = recipe.getId();
        this.recipe = recipe;
        this.entry = entry;
        this.runicEnergy = runicEnergy;
        this.result = recipe.getResultItem(RegistryAccess.EMPTY);
        this.ingredients = recipe.getIngredients();
        this.isSimple = recipe.getIngredients().stream().allMatch(Ingredient::isSimple);
    }


    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < pInv.getContainerSize(); ++j) {
            ItemStack itemstack = pInv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return recipe.assemble(pContainer, pRegistryAccess);
    }

    public boolean playerHasNeededEntry(Player player) {
        ConcurrentMap<ResourceLocation, Set<ResourceLocation>> entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).readEntries;
        for (Map.Entry<ResourceLocation, Set<ResourceLocation>> i : entries.entrySet()) {
            for (ResourceLocation o : i.getValue()) {
                if (o.toString().equals(entry)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RUNICCRAFTING.get();
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public Map<String, Float> getRunicEnergyCost() {
        return runicEnergy;
    }

    public static class Serializer implements RecipeSerializer<ShapelessRunicRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"shapelessrunicrecipe");

        @Override
        public ShapelessRunicRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe recipe = RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
            String entry = json.get("entry").getAsString();
            HashMap<String, Float> runicEnergy = new HashMap<>();
            for (JsonElement i : json.getAsJsonArray("runicenergy")) {
                runicEnergy.put(i.getAsJsonObject().get("type").getAsString(), i.getAsJsonObject().get("amount").getAsFloat());
            }
            return new ShapelessRunicRecipe(recipe, entry, runicEnergy);
        }

        @Override
        public ShapelessRunicRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe recipe = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf);
            String entry = buf.readUtf();
            Map<String, Float> map = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat);
            return new ShapelessRunicRecipe(recipe, entry, map);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessRunicRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe.recipe);
            buf.writeUtf(recipe.entry);
            buf.writeMap(recipe.runicEnergy, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}