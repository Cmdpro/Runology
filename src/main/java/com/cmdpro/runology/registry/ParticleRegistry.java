package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE,
            Runology.MODID);
    public static final Supplier<SimpleParticleType> SHATTER = register("shatter", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SMALL_SHATTER = register("small_shatter", () -> new SimpleParticleType(false));

    private static <T extends ParticleType<?>> Supplier<T> register(final String name, final Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }
}
