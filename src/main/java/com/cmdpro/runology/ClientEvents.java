package com.cmdpro.runology;

import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.BlinkC2SPacket;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID)
public class ClientEvents {
    static boolean blinkWasDown = false;
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player != null) {
            if (ClientSetupEvents.BLINK_MAPPING.get().isDown()) {
                Player player = Minecraft.getInstance().player;
                if (player.getInventory().armor.stream().anyMatch((a) -> a.is(ItemRegistry.BLINK_BOOTS.get())) && player.getData(AttachmentTypeRegistry.BLINK_COOLDOWN) <= 0) {
                    Vec3 offset = new Vec3(0, player.getEyeHeight(), 0);
                    for (int i = 0; i < 25 * 4; i++) {
                        if (!player.isFree(offset.x, offset.y, offset.z)) {
                            break;
                        }
                        offset = offset.add(player.getLookAngle().multiply(0.25, 0.25, 0.25));
                    }
                    for (int i = 0; i < 10; i++) {
                        Vec3 particlePos = player.position().add(new Vec3(offset.x, offset.y + 0.1, offset.z));
                        particlePos = particlePos.add((player.getRandom().nextFloat() - 0.5f) * 0.5f, (player.getRandom().nextFloat() - 0.5f) * 0.2f, (player.getRandom().nextFloat() - 0.5f) * 0.5f);
                        player.level().addParticle(ParticleRegistry.SHATTER.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
                    }
                }
            }
            if (!ClientSetupEvents.BLINK_MAPPING.get().isDown() && blinkWasDown) {
                ModMessages.sendToServer(new BlinkC2SPacket());
            }
            blinkWasDown = ClientSetupEvents.BLINK_MAPPING.get().isDown();
            ClientSetupEvents.playerPowerModeShader.setActive(Minecraft.getInstance().player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE));
        }
    }
}
