package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.networking.Message;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.registry.SoundRegistry;
import com.cmdpro.runology.screen.FalseDeathScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BlinkC2SPacket() implements Message {
    public static BlinkC2SPacket read(RegistryFriendlyByteBuf buf) {
        return new BlinkC2SPacket();
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer player, IPayloadContext ctx) {
        if (player.getInventory().armor.stream().anyMatch((a) -> a.is(ItemRegistry.BLINK_BOOTS.get())) && player.getData(AttachmentTypeRegistry.BLINK_COOLDOWN) <= 0) {
            Vec3 offset = new Vec3(0, player.getEyeHeight(), 0);
            for (int i = 0; i < 25 * 4; i++) {
                if (!player.isFree(offset.x, offset.y, offset.z)) {
                    break;
                }
                Vec3 pos = player.position().add(offset);
                ((ServerLevel)player.level()).sendParticles(ParticleRegistry.SHATTER.get(), pos.x, pos.y, pos.z, 10, 0.25, 0.1, 0.25, 0.05);
                offset = offset.add(player.getLookAngle().multiply(0.25, 0.25, 0.25));
            }
            player.level().playSound(null, player.blockPosition(), SoundRegistry.BLINK_TELEPORT.value(), SoundSource.PLAYERS);
            player.teleportRelative(offset.x, offset.y+0.1, offset.z);
            player.level().playSound(null, player.blockPosition(), SoundRegistry.BLINK_TELEPORT.value(), SoundSource.PLAYERS);
            player.setData(AttachmentTypeRegistry.BLINK_COOLDOWN, 30);
        }
    }

    public static void write(RegistryFriendlyByteBuf buf, BlinkC2SPacket obj) {

    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final Type<BlinkC2SPacket> TYPE = new Type<>(Runology.locate("blink"));
}