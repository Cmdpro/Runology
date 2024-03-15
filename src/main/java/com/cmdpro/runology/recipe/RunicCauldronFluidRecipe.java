package com.cmdpro.runology.recipe;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicCauldronFluidRecipe implements IHasRequiredKnowledge, IRunicCauldronRecipe {
    private final ResourceLocation id;
    private final FluidStack output;
    private final Ingredient input;
    private final FluidStack fluidInput;
    private final String entry;

    public RunicCauldronFluidRecipe(ResourceLocation id, FluidStack output,
                                    Ingredient input, FluidStack fluidInput, String entry) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.fluidInput = fluidInput;
        this.entry = entry;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }
    @Override
    public FluidStack getFluidInput() {
        return fluidInput;
    }
    @Override
    public FluidStack getFluidOutput() {
        return output;
    }
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isFluidOutput() {
        return true;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    public static class Type implements RecipeType<RunicCauldronFluidRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "runiccauldronfluid";
    }

    public static class Serializer implements RecipeSerializer<RunicCauldronFluidRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"runiccauldronfluid");

        @Override
        public RunicCauldronFluidRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation fluidId1 = ResourceLocation.tryParse(json.get("output").getAsJsonObject().get("id").getAsString());
            int fluidAmount1 = json.get("output").getAsJsonObject().get("amount").getAsInt();
            FluidStack output = new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId1), fluidAmount1);
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            ResourceLocation fluidId = ResourceLocation.tryParse(json.get("fluidInput").getAsJsonObject().get("id").getAsString());
            FluidStack fluidInput = new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId), 1000);
            String entry = GsonHelper.getAsString(json, "entry");
            return new RunicCauldronFluidRecipe(id, output, input, fluidInput, entry);
        }

        @Override
        public RunicCauldronFluidRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            FluidStack fluidInput = buf.readFluidStack();
            FluidStack output = buf.readFluidStack();
            String entry = buf.readUtf();
            return new RunicCauldronFluidRecipe(id, output, input, fluidInput, entry);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RunicCauldronFluidRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidInput);
            buf.writeFluidStack(recipe.getFluidOutput());
            buf.writeUtf(recipe.entry);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}