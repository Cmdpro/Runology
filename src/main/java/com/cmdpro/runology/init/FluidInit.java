package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidInit {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Runology.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCELIQUIDSOULS = FLUIDS.register("liquidsouls",
            () -> new ForgeFlowingFluid.Source(FluidInit.LIQUIDSOULSFLUIDPROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWINGLIQUIDSOULS = FLUIDS.register("flowingliquidsouls",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.LIQUIDSOULSFLUIDPROPERTIES));
    public static final ForgeFlowingFluid.Properties LIQUIDSOULSFLUIDPROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeInit.LIQUIDSOULSFLUIDTYPE, SOURCELIQUIDSOULS, FLOWINGLIQUIDSOULS)
            .slopeFindDistance(1).levelDecreasePerBlock(3).block(BlockInit.LIQUIDSOULSBLOCK)
            .bucket(ItemInit.LIQUIDSOULSBUCKET);


    public static final RegistryObject<FlowingFluid> SOURCETRANSMUTATIVESOLUTION = FLUIDS.register("transmutativesolution",
            () -> new ForgeFlowingFluid.Source(FluidInit.TRANSMUTATIVESOLUTIONFLUIDPROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWINGTRANSMUTATIVESOLUTION = FLUIDS.register("flowingtransmutativesolution",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.TRANSMUTATIVESOLUTIONFLUIDPROPERTIES));
    public static final ForgeFlowingFluid.Properties TRANSMUTATIVESOLUTIONFLUIDPROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypeInit.TRANSMUTATIVESOLUTIONFLUIDTYPE, SOURCETRANSMUTATIVESOLUTION, FLOWINGTRANSMUTATIVESOLUTION)
            .slopeFindDistance(1).levelDecreasePerBlock(3).block(BlockInit.TRANSMUTATIVESOLUTIONBLOCK)
            .bucket(ItemInit.TRANSMUTATIVESOLUTIONBUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
