package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.RunicEnergyType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.function.Supplier;

public class RunicEnergyInit {
    public static final DeferredRegister<RunicEnergyType> RUNIC_ENERGY_TYPES = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "runicenergytypes"), Runology.MOD_ID);

    public static final RegistryObject<RunicEnergyType> EARTH = register("earth", () -> new RunicEnergyType(new Color(130, 86, 51)));
    public static final RegistryObject<RunicEnergyType> WATER = register("water", () -> new RunicEnergyType(new Color(156, 220, 255)));
    public static final RegistryObject<RunicEnergyType> AIR = register("air", () -> new RunicEnergyType(new Color(241, 255, 222)));
    public static final RegistryObject<RunicEnergyType> FIRE = register("fire", () -> new RunicEnergyType(new Color(221, 132, 59)));
    public static final RegistryObject<RunicEnergyType> ENERGY = register("energy", () -> new RunicEnergyType(new Color(245, 202, 118)));
    public static final RegistryObject<RunicEnergyType> ICE = register("ice", () -> new RunicEnergyType(new Color(214, 240, 255)));
    public static final RegistryObject<RunicEnergyType> PLANT = register("plant", () -> new RunicEnergyType(new Color(84, 195, 0)));
    public static final RegistryObject<RunicEnergyType> VOID = register("void", () -> new RunicEnergyType(new Color(146, 70, 156)));
    public static final RegistryObject<RunicEnergyType> INSTABILITY = register("instability", () -> new RunicEnergyType(new Color(0, 0, 0)));
    public static final RegistryObject<RunicEnergyType> PURITY = register("purity", () -> new RunicEnergyType(new Color(255, 255, 255)));
    private static <T extends RunicEnergyType> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return RUNIC_ENERGY_TYPES.register(name, item);
    }
}
