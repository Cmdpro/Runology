package com.cmdpro.runology.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;
import java.util.function.Supplier;

public class DisplayPrismaticBulletEffectsS2CPacket {
    private final Vec3 pos;
    private final Color color;
    private final int effectType;

    public DisplayPrismaticBulletEffectsS2CPacket(Vec3 pos, Color color, int effectType) {
        this.pos = pos;
        this.color = color;
        this.effectType = effectType;
    }

    public DisplayPrismaticBulletEffectsS2CPacket(FriendlyByteBuf buf) {
        this.pos = new Vec3(buf.readVector3f());
        float r = buf.readFloat();
        float g = buf.readFloat();
        float b = buf.readFloat();
        float a = buf.readFloat();
        this.color = new Color(r, g, b, a);
        this.effectType = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVector3f(pos.toVector3f());
        buf.writeFloat((float)color.getRed()/255);
        buf.writeFloat((float)color.getGreen()/255);
        buf.writeFloat((float)color.getBlue()/255);
        buf.writeFloat((float)color.getAlpha()/255);
        buf.writeInt(effectType);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePacket(this, supplier));
        });
        context.setPacketHandled(true);
    }
    public static class ClientPacketHandler {
        public static void handlePacket(DisplayPrismaticBulletEffectsS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (msg.effectType == 0) {
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setScaleData(GenericParticleData.create(0.25f).build())
                        .setColorData(ColorParticleData.create(msg.color, msg.color).build())
                        .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
                        .setLifetime(20)
                        .enableNoClip()
                        .spawn(Minecraft.getInstance().player.level(), msg.pos.x, msg.pos.y, msg.pos.z);
            } else if (msg.effectType == 1) {
                for (int i = 0; i < 25; i++) {
                    WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                            .setScaleData(GenericParticleData.create(0.25f).build())
                            .setColorData(ColorParticleData.create(msg.color, Color.BLACK).build())
                            .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
                            .setRandomMotion(0.2f)
                            .setLifetime(20)
                            .enableNoClip()
                            .spawn(Minecraft.getInstance().player.level(), msg.pos.x, msg.pos.y, msg.pos.z);
                }
            }
        }
    }
}
