package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.ClientRunologyUtil;
import com.cmdpro.runology.api.RunologyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Math;
import org.lwjgl.system.MathUtil;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

public class DisplayInstabilityGenerationS2CPacket {
    private final Vec3 pos;
    private final Vec3 dir;
    private final float instabilityAmount;
    public DisplayInstabilityGenerationS2CPacket(Vec3 pos, Vec3 dir, float instabilityAmount) {
        this.pos = pos;
        this.dir = dir;
        this.instabilityAmount = instabilityAmount;
    }

    public DisplayInstabilityGenerationS2CPacket(FriendlyByteBuf buf) {
        this.pos = new Vec3(buf.readVector3f());
        this.dir = new Vec3(buf.readVector3f());
        this.instabilityAmount = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVector3f(pos.toVector3f());
        buf.writeVector3f(dir.toVector3f());
        buf.writeFloat(instabilityAmount);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                for (int i = 0; i < Math.clamp(instabilityAmount, 5, 50); i++) {
                    WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                            .setScaleData(GenericParticleData.create(0.25f).build())
                            .setColorData(ColorParticleData.create(Color.DARK_GRAY, Color.BLACK).build())
                            .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
                            .addMotion(dir.x + (RandomUtils.nextDouble(0d, 0.1d)-0.05d), dir.y + (RandomUtils.nextDouble(0d, 0.1d)-0.05d), dir.z + (RandomUtils.nextDouble(0d, 0.1d)-0.05d))
                            .setLifetime(20)
                            .enableNoClip()
                            .spawn(Minecraft.getInstance().player.level(), pos.x+(RandomUtils.nextDouble(0d, 0.25d)-0.125d), pos.y, pos.z+(RandomUtils.nextDouble(0d, 0.25d)-0.125d));
                }
            });
        });
        context.setPacketHandled(true);
    }
}
