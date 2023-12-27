package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.RunicEnergyType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.function.Supplier;

public class RunicEnergyInit {
    public static final DeferredRegister<RunicEnergyType> RUNIC_ENERGY_TYPES = DeferredRegister.create(new ResourceLocation(RunicArts.MOD_ID, "runicenergytypes"), RunicArts.MOD_ID);

    public static final RegistryObject<RunicEnergyType> EARTH = register("earth", () -> new RunicEnergyType(new Color(0f, 1f, 0f)));
    public static final RegistryObject<RunicEnergyType> WATER = register("water", () -> new RunicEnergyType(new Color(0f, 0f, 1f)));
    public static final RegistryObject<RunicEnergyType> AIR = register("air", () -> new RunicEnergyType(new Color(1f, 1f, 1f)));
    public static final RegistryObject<RunicEnergyType> FIRE = register("fire", () -> new RunicEnergyType(new Color(1f, 0f, 0f)));
    private static <T extends RunicEnergyType> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return RUNIC_ENERGY_TYPES.register(name, item);
    }
}
