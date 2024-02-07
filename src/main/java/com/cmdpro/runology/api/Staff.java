package com.cmdpro.runology.api;

import net.minecraft.world.item.Item;

public class Staff extends Item {
    public int magicLevel;
    public Staff(Properties properties, int magicLevel) {
        super(properties);
        this.magicLevel = magicLevel;
    }
}
