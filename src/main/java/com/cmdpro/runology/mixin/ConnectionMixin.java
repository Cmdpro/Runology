package com.cmdpro.runology.mixin;

import com.cmdpro.runology.Runology;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "sendPacket", at = @At("HEAD"), remap = true)
    public void send(Packet<?> pPacket, PacketSendListener pSendListener, CallbackInfo ci) {
        if (pPacket instanceof ServerboundCustomPayloadPacket) {
            Runology.LOGGER.info("custom :O" + ((ServerboundCustomPayloadPacket) pPacket).getIdentifier().getNamespace());
        } else if(pPacket instanceof ClientboundCustomPayloadPacket) {
            Runology.LOGGER.info("custom :O" + ((ClientboundCustomPayloadPacket) pPacket).getIdentifier().getNamespace());
        }
        else {
            Runology.LOGGER.info(pPacket.getClass().getName());
        }
    }
}