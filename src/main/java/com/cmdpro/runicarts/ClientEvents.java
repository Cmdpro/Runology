package com.cmdpro.runicarts;

import com.cmdpro.runicarts.entity.SoulKeeper;
import com.cmdpro.runicarts.init.SoundInit;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.networking.ModMessages;
import com.cmdpro.runicarts.networking.packet.PlayerDoubleJumpC2SPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RunicArts.MOD_ID)
public class ClientEvents {
    public static SimpleSoundInstance music;
    static boolean jumpJustDown = false;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.keyJump.isDown()) {
            if (ClientPlayerData.getCanDoubleJump() && !Minecraft.getInstance().player.onGround() && !jumpJustDown && ClientPlayerData.getPlayerSouls() >= 2) {
                ModMessages.sendToServer(new PlayerDoubleJumpC2SPacket());
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.5, mc.player.getDeltaMovement().z);
            }
            jumpJustDown = true;
        } else {
            jumpJustDown = false;
        }
        if (event.phase == TickEvent.Phase.END && mc.level != null)
        {
            boolean playMusic = false;
            SoundEvent mus = SoundInit.SOULKEEPERPHASE1.get();
            for (Entity i : mc.level.entitiesForRendering()) {
                if (i instanceof SoulKeeper) {
                    playMusic = true;
                    if (i.getEntityData().get(((SoulKeeper)i).IS_PHASE2)) {
                        mus = SoundInit.SOULKEEPERPHASE2.get();
                    }
                }
            }
            SoundManager manager = mc.getSoundManager();
            if (manager.isActive(music))
            {
                mc.getMusicManager().stopPlaying();
                if (!playMusic)
                {
                    manager.stop(music);
                }
                if (!music.getLocation().equals(mus.getLocation())) {
                    manager.stop(music);
                }
            } else {
                if (!manager.isActive(music) && playMusic)
                {
                    music = SimpleSoundInstance.forMusic(mus);
                    manager.play(music);
                }
            }
        }
    }
}
