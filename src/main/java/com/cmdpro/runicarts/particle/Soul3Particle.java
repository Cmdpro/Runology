package com.cmdpro.runicarts.particle;

import com.cmdpro.runicarts.init.ParticleInit;
import com.klikli_dev.modonomicon.util.Codecs;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;

import java.util.Locale;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class Soul3Particle extends TextureSheetParticle {
    public float startQuadSize;
    public Soul3ParticleOptions options;
    protected Soul3Particle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                            SpriteSet spriteSet, double xd, double yd, double zd, Soul3ParticleOptions options) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 0.8F;
        this.xd = xd+RandomUtils.nextFloat(0f, 0.5f)-0.25f;
        this.yd = yd+0.75;
        this.zd = zd+RandomUtils.nextFloat(0f, 0.5f)-0.25f;
        this.quadSize *= 1.25F;
        startQuadSize = this.quadSize;
        this.lifetime = 2;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.alpha = 1f;
        this.hasPhysics = true;
        this.options = options;
        this.spd = 0.25f;
        ticks = 0;
    }
    public float spd;
    public int ticks;
    @Override
    public void tick() {
        super.tick();
        ticks++;
        if (ticks >= 10) {
            hasPhysics = false;
            Vec3 pos = getPos();
            Vec3 plrPos = level.getPlayerByUUID(options.player).position();
            pos = pos.lerp(plrPos, spd);
            setPos(pos.x, pos.y, pos.z);
            spd += 0.05f;
            if (pos.distanceTo(plrPos) > 0.1f) {
                age = 0;
            }
        } else {
            age = 0;
            hasPhysics = true;
            yd -= ticks*0.01f;
        }
        level.addParticle(ParticleInit.SOUL.get(), x, y, z, 0, 0, 0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return SoulParticle.SOULRENDER;
    }
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<Soul3ParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(Soul3ParticleOptions particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new Soul3Particle(level, x, y, z, this.sprites, dx, dy, dz, particleType);
        }
    }
}
