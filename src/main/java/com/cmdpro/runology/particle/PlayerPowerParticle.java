package com.cmdpro.runology.particle;

import com.cmdpro.databank.rendering.RenderHandler;
import com.cmdpro.runology.RenderEvents;
import com.cmdpro.runology.shaders.RunologyRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.apache.commons.lang3.RandomUtils;

public class PlayerPowerParticle extends TextureSheetParticle {
    public float startQuadSize;
    public float rot;
    protected PlayerPowerParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                  SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 1.25F;
        startQuadSize = this.quadSize;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);
        rot = RandomUtils.nextFloat(0f, 2f)-1f;
        this.hasPhysics = false;
    }
    @Override
    public void tick() {
        super.tick();
        fadeOut();
        xd /= 5;
        zd /= 5;
        this.oRoll = this.roll;
        roll += rot/5f;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        super.render(RenderEvents.createShatterOutlineBufferSource().getBuffer(RunologyRenderTypes.PLAYER_POWER_PARTICLE), pRenderInfo, pPartialTicks);
        super.render(RenderHandler.createBufferSource().getBuffer(RunologyRenderTypes.PLAYER_POWER_PARTICLE), pRenderInfo, pPartialTicks);
    }

    private void fadeOut() {
        this.quadSize = (-(1/(float)lifetime) * age + 1)*startQuadSize;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new PlayerPowerParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
