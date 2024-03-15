package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.fluid.BaseFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class FluidTypeInit {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Runology.MOD_ID);
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    public static final RegistryObject<FluidType> LIQUIDSOULSFLUIDTYPE = register("liquidsouls",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5), new Vector3f(161, 187, 203), 0xffffffff,
            new ResourceLocation(Runology.MOD_ID, "block/fluid/liquidsouls/still"), new ResourceLocation(Runology.MOD_ID, "block/fluid/liquidsouls/flowing"), null);
    public static final RegistryObject<FluidType> TRANSMUTATIVESOLUTIONFLUIDTYPE = register("transmutativesolution",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5), new Vector3f(41, 0, 82), 0xffffffff,
            new ResourceLocation(Runology.MOD_ID, "block/fluid/transmutativesolution/still"), new ResourceLocation(Runology.MOD_ID, "block/fluid/transmutativesolution/flowing"), new ResourceLocation(Runology.MOD_ID, "block/fluid/transmutativesolution/overlay"));



    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties, Vector3f color, int tint, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(still, flowing, overlay,
                tint, new Vector3f(color.x / 255f, color.y / 255f, color.z / 255f), properties));
    }
}
