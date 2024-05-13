package com.cmdpro.runology.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class RunicEnergyType {
    public Color color;
    public ResourceLocation core;
    public RunicEnergyType(Color color) {
        this.color = color;
    }
    public RunicEnergyType(Color color, ResourceLocation core) {
        this.color = color;
        this.core = core;
    }
}
