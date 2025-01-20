package com.cmdpro.runology.data.runechiseling;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class RuneChiselingResult {
    public ResourceLocation id;
    public Block input;
    public ResourceLocation rune;
    public Block output;
    public RuneChiselingResult(ResourceLocation id, Block input, ResourceLocation rune, Block output) {
        this.id = id;
        this.input = input;
        this.rune = rune;
        this.output = output;
    }
}
