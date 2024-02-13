package com.cmdpro.runology.item;

import com.cmdpro.runology.init.ItemInit;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier REALITYSLICER = new ForgeTier(4, 800, 8f,
            8f, 15, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ItemInit.SHATTEREDSOUL.get()));
}
