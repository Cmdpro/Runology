package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.api.ClientRunologyUtil;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.integration.modonomicon.bookconditions.BookAnalyzeTaskCondition;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;

import java.awt.*;
import java.util.function.Supplier;

public class DisplayEnderTransporterParticleLineS2CPacket {
    private final Vec3 pos1;
    private final Vec3 pos2;
    private final Color color;

    public DisplayEnderTransporterParticleLineS2CPacket(Vec3 pos1, Vec3 pos2, Color color) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.color = color;
    }

    public DisplayEnderTransporterParticleLineS2CPacket(FriendlyByteBuf buf) {
        this.pos1 = new Vec3(buf.readVector3f());
        this.pos2 = new Vec3(buf.readVector3f());
        float r = buf.readFloat();
        float g = buf.readFloat();
        float b = buf.readFloat();
        float a = buf.readFloat();
        this.color = new Color(r, g, b, a);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVector3f(pos1.toVector3f());
        buf.writeVector3f(pos2.toVector3f());
        buf.writeFloat((float)color.getRed()/255);
        buf.writeFloat((float)color.getGreen()/255);
        buf.writeFloat((float)color.getBlue()/255);
        buf.writeFloat((float)color.getAlpha()/255);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePacket(this, supplier));
        });
        context.setPacketHandled(true);
    }
    public static class ClientPacketHandler {
        public static void handlePacket(DisplayEnderTransporterParticleLineS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
            WorldParticleOptions options = new WorldParticleOptions(LodestoneParticleRegistry.WISP_PARTICLE.get());
            options.colorData = ColorParticleData.create(msg.color, msg.color).build();
            options.scaleData = GenericParticleData.create(0.25f).build();
            ClientRunologyUtil.drawLine(options, msg.pos1, msg.pos2, Minecraft.getInstance().player.level(), 0.1f);
        }
    }
}
