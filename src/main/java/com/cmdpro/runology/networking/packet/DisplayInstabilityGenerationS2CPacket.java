package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.api.ClientRunologyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.RandomUtils;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;

import java.awt.*;
import java.util.function.Supplier;

public class DisplayInstabilityGenerationS2CPacket {
    private final Vec3 pos;
    private final Vec3 dir;

    public DisplayInstabilityGenerationS2CPacket(Vec3 pos, Vec3 dir) {
        this.pos = pos;
        this.dir = dir;
    }

    public DisplayInstabilityGenerationS2CPacket(FriendlyByteBuf buf) {
        this.pos = new Vec3(buf.readVector3f());
        this.dir = new Vec3(buf.readVector3f());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVector3f(pos.toVector3f());
        buf.writeVector3f(dir.toVector3f());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                for (int i = 0; i < 25; i++) {
                    WorldParticleOptions options = new WorldParticleOptions(LodestoneParticleRegistry.WISP_PARTICLE.get());
                    options.colorData = ColorParticleData.create(Color.BLACK, Color.BLACK).build();
                    options.scaleData = GenericParticleData.create(0.25f).build();
                    Minecraft.getInstance().player.level().addParticle(options, pos.x, pos.y, pos.z, dir.x + RandomUtils.nextFloat(-0.1f, 0.1f), dir.y + RandomUtils.nextFloat(-0.1f, 0.1f), dir.z + RandomUtils.nextFloat(-0.1f, 0.1f));
                }
            });
        });
        context.setPacketHandled(true);
    }
}
