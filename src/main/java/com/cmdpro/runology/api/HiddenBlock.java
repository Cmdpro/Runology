package com.cmdpro.runology.api;

import net.minecraft.world.level.block.Block;

public interface HiddenBlock {
    boolean entry();
    Block hideAs();
}
