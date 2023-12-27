package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.SoulcasterEffect;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.soulcastereffects.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SoulcasterEffectInit {
    public static final DeferredRegister<SoulcasterEffect> SOULCASTER_EFFECTS = DeferredRegister.create(new ResourceLocation(RunicArts.MOD_ID, "soulcaster_effects"), RunicArts.MOD_ID);

    public static final RegistryObject<SoulcasterEffect> ICE = register("ice", () -> new IceSoulcasterEffect());
    public static final RegistryObject<SoulcasterEffect> DEATH = register("death", () -> new DeathSoulcasterEffect());
    public static final RegistryObject<SoulcasterEffect> ENDER = register("ender", () -> new EnderSoulcasterEffect());
    public static final RegistryObject<SoulcasterEffect> FLAME = register("flame", () -> new FlameSoulcasterEffect());
    public static final RegistryObject<SoulcasterEffect> LIFE = register("life", () -> new LifeSoulcasterEffect());
    private static <T extends SoulcasterEffect> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return SOULCASTER_EFFECTS.register(name, item);
    }
}
