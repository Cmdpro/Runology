package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RunicArts.MOD_ID);

    public static final RegistryObject<SimpleParticleType> SOUL =
            PARTICLE_TYPES.register("soul", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SOUL2 =
            PARTICLE_TYPES.register("soul2", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType> SOUL3 =
            PARTICLE_TYPES.register("soul3", () -> new ParticleType<Soul3ParticleOptions>(false, Soul3ParticleOptions.DESERIALIZER) {
                @Override
                public Codec<Soul3ParticleOptions> codec() {
                    return Soul3ParticleOptions.CODEC;
                }
            });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
