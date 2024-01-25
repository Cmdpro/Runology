package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagInit {
    public static class Blocks {
        public static final TagKey<Block> SHATTERSTONEREPLACEABLE = tag("shatterstonereplaceable");
        public static final TagKey<Block> SHATTERWOODREPLACEABLE = tag("shatterwoodreplaceable");
        public static final TagKey<Block> SHATTERREMOVES = tag("shatterremoves");
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Runology.MOD_ID, name));
        }
    }
    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Runology.MOD_ID, name));
        }
    }
}
