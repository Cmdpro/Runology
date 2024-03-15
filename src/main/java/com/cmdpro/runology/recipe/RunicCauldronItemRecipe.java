package com.cmdpro.runology.recipe;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.fluid.ForgeFluidHelper;
import com.klikli_dev.modonomicon.platform.services.FluidHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.util.JsonUtil;

public class RunicCauldronItemRecipe implements IRunicCauldronRecipe, IHasRequiredKnowledge {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final FluidStack fluidInput;
    private final String entry;

    public RunicCauldronItemRecipe(ResourceLocation id, ItemStack output,
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
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }
    @Override
    public FluidStack getFluidInput() {
        return fluidInput;
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
        return false;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    public static class Type implements RecipeType<RunicCauldronItemRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "runiccauldronitem";
    }

    public static class Serializer implements RecipeSerializer<RunicCauldronItemRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"runiccauldronitem");

        @Override
        public RunicCauldronItemRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            ResourceLocation fluidId = ResourceLocation.tryParse(json.get("fluidInput").getAsJsonObject().get("id").getAsString());
            int fluidAmount = json.get("fluidInput").getAsJsonObject().get("amount").getAsInt();
            FluidStack fluidInput = new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId), fluidAmount);
            String entry = GsonHelper.getAsString(json, "entry");
            return new RunicCauldronItemRecipe(id, output, input, fluidInput, entry);
        }

        @Override
        public RunicCauldronItemRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            FluidStack fluidInput = buf.readFluidStack();
            ItemStack output = buf.readItem();
            String entry = buf.readUtf();
            return new RunicCauldronItemRecipe(id, output, input, fluidInput, entry);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RunicCauldronItemRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeFluidStack(recipe.fluidInput);
            buf.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
            buf.writeUtf(recipe.entry);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}