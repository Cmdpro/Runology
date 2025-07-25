package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagRegistry {
    public static class Blocks {
        public static final TagKey<Block> SHATTER_GROW_PLANTS_ON = tag("shatter_grow_plants_on");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(Runology.locate(name));
        }
    }
    public static class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(Runology.locate(name));
        }
    }
    public static class EntityTypes {
        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, Runology.locate(name));
        }
    }
}
