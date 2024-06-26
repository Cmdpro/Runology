package com.cmdpro.runology.item;

import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.TagInit;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier REALITY = new ForgeTier(4, 800, 8f,
            8f, 15, TagInit.Blocks.REALITYBREAKERMINEABLE,
            () -> Ingredient.of(ItemInit.SHATTEREDSOUL.get()));
    public static final ForgeTier ANCIENTDRAGONSBLADE = new ForgeTier(4, 1500, 8f,
            6f, 15, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ItemInit.DRAGONIUMINGOT.get()));
}
