package com.cmdpro.runicarts.particle;

import com.cmdpro.runicarts.init.ParticleInit;
import com.klikli_dev.modonomicon.util.Codecs;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;
import java.util.UUID;

public class Soul3ParticleOptions implements ParticleOptions {
    public UUID player;
    public Soul3ParticleOptions(UUID player) {
        this.player = player;
    }
    @Override
    public ParticleType<?> getType() {
        return ParticleInit.SOUL3.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeUUID(player);
    }
    public static final Codec<Soul3ParticleOptions> CODEC = RecordCodecBuilder.create((p_253370_) -> {
        return p_253370_.group(Codecs.UUID.fieldOf("player").forGetter((p_253371_) -> {
            return p_253371_.player;
        })).apply(p_253370_, Soul3ParticleOptions::new);
    });
    public static final ParticleOptions.Deserializer<Soul3ParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<Soul3ParticleOptions>() {
        @Override
        public Soul3ParticleOptions fromCommand(ParticleType<Soul3ParticleOptions> options, StringReader reader) throws CommandSyntaxException {
            UUID uuid = UUID.fromString(reader.readString());
            return new Soul3ParticleOptions(uuid);
        }
        @Override
        public Soul3ParticleOptions fromNetwork(ParticleType<Soul3ParticleOptions> options, FriendlyByteBuf buf) {
            UUID uuid = buf.readUUID();
            return new Soul3ParticleOptions(uuid);
        }
    };

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.player);
    }
}