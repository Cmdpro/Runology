package com.cmdpro.runology.api;

import net.minecraft.world.item.Item;

public class EmpoweredGauntlet extends Item {
    public Item returnsTo;
    public EmpoweredGauntlet(Properties properties, Item returnsTo) {
        super(properties);
        this.returnsTo = returnsTo;
    }
}
