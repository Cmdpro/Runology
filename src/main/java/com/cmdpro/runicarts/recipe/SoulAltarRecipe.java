package com.cmdpro.runicarts.recipe;


import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.init.ItemInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;


public class SoulAltarRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> input;
    public final String entry;
    public final boolean mustRead;
    public final boolean locked;
    public final boolean createFocus;

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemInit.SOULALTAR_ITEM.get());
    }

    public SoulAltarRecipe(ResourceLocation id, ItemStack output,
                           NonNullList<Ingredient> input, boolean createFocus, String entry, boolean mustRead, boolean locked) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.createFocus = createFocus;
        this.entry = entry;
        this.mustRead = mustRead;
        this.locked = locked;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        for (Ingredient i : this.input) {
            nonnulllist.add(i);
        }
        return nonnulllist;
    }
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        ItemStack focus = pContainer.getItem(0);
        if (focus.is(ItemInit.SOULFOCUS.get())) {
            if (focus.hasTag()) {
                if (focus.getTag().contains("recipe")) {
                    if (ResourceLocation.tryParse(focus.getTag().getString("recipe")).equals(this.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
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

    public static class Type implements RecipeType<SoulAltarRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "soulaltar";
    }


    public static class Serializer implements RecipeSerializer<SoulAltarRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(RunicArts.MOD_ID, Type.ID);
        @Override
        public SoulAltarRecipe fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> input = itemsFromJson(GsonHelper.getAsJsonArray(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            boolean createFocus = true;
            String entry = "";
            boolean mustRead = true;
            boolean locked = false;
            if (json.has("createFocus")) {
                createFocus = json.get("createFocus").getAsBoolean();
            }
            if (createFocus) {
                if (json.has("locked")) {
                    locked = json.get("locked").getAsBoolean();
                    if (locked) {
                        entry = json.get("entry").getAsString();
                        if (json.has("mustRead")) {
                            mustRead = json.get("mustRead").getAsBoolean();
                        }
                    }
                }
            }
            return new SoulAltarRecipe(id, output, input, createFocus, entry, mustRead, locked);
        }
        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i), false);
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public SoulAltarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buf));
            }
            ItemStack output = buf.readItem();
            boolean createFocus = buf.readBoolean();
            String entry = buf.readUtf();
            boolean mustRead = buf.readBoolean();
            boolean locked = buf.readBoolean();
            return new SoulAltarRecipe(id, output, nonnulllist, createFocus, entry, mustRead, locked);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SoulAltarRecipe recipe) {
            buf.writeVarInt(recipe.input.size());
            for(Ingredient ingredient : recipe.input) {
                ingredient.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
            buf.writeBoolean(recipe.createFocus);
            buf.writeUtf(recipe.entry);
            buf.writeBoolean(recipe.mustRead);
            buf.writeBoolean(recipe.locked);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}