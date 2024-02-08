package com.cmdpro.runology.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class UpgradeItem extends Item {
    public ResourceLocation upgrade;
    public UpgradeItem(Properties pProperties, ResourceLocation upgrade) {
        super(pProperties);
        this.upgrade = upgrade;
    }
}
