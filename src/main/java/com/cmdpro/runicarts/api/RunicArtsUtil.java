package com.cmdpro.runicarts.api;

import com.cmdpro.runicarts.init.EntityInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RunicArtsUtil {
    public static Supplier<IForgeRegistry<RunicEnergyType>> RUNIC_ENERGY_TYPES_REGISTRY = null;
    public static List<Item> SOULCASTER_CRYSTALS = new ArrayList<>();
}
