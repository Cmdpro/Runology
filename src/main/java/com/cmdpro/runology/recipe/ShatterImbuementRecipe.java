package com.cmdpro.runology.recipe;

import com.cmdpro.runology.registry.RecipeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class ShatterImbuementRecipe implements Recipe<RecipeInput> {
    private final ItemStack output;
    private final Ingredient input;
    private final int tier;

    public ShatterImbuementRecipe(ItemStack output,
                                  Ingredient input, int tier) {
        this.output = output;
        this.input = input;
        this.tier = tier;
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }
    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public int getTier() {
        return tier;
    }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.SHATTER_IMBUEMENT_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ShatterImbuementRecipe> {
        public static final MapCodec<ShatterImbuementRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
                Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
                Codec.INT.optionalFieldOf("tier", 0).forGetter(r -> r.tier)
        ).apply(instance, ShatterImbuementRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ShatterImbuementRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, obj) -> {
                    ItemStack.STREAM_CODEC.encode(buf, obj.output);
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, obj.input);
                    buf.writeInt(obj.tier);
                },
                (buf) -> {
                    ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
                    Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int tier = buf.readInt();
                    return new ShatterImbuementRecipe(output, input, tier);
                }
        );

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<ShatterImbuementRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShatterImbuementRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}