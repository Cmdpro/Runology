package com.cmdpro.runology;

import com.cmdpro.runology.api.ClientRunologyUtil;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.entity.RunicOverseer;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.SoundInit;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.lighting.LightEventListener;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.mixin.client.GameRendererMixin;
import team.lodestar.lodestone.systems.postprocess.LodestoneGlslPreprocessor;
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

import java.awt.*;
import java.io.IOException;

import static com.mojang.blaze3d.platform.GlConst.GL_DRAW_FRAMEBUFFER;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MOD_ID)
public class ClientEvents {
    public static SimpleSoundInstance music;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            for (Player i : Minecraft.getInstance().level.players()) {
                if (i.getUseItem().is(ItemInit.LANTERNOFFLAMES.get())) {
                    Vec3 pos = i.getBoundingBox().getCenter().subtract(event.getCamera().getPosition());
                    event.getPoseStack().translate(pos.x, pos.y, pos.z);
                    ClientRunologyUtil.drawSphere(VFXBuilders.createWorld(), event.getPoseStack(), Color.RED, 0.25f, 5, 30, 30);
                    event.getPoseStack().translate(-pos.x, -pos.y, -pos.z);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        boolean echoGogglesActive = false;
        if (Minecraft.getInstance().player != null) {
            echoGogglesActive = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD).is(ItemInit.ECHOGOGGLES.get());
        }
        ClientModEvents.echoGogglesProcessor.setActive(echoGogglesActive);
        Minecraft mc = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END && mc.level != null)
        {
            boolean playMusic = false;
            SoundEvent mus = SoundInit.RUNICOVERSEER.get();
            for (Entity i : mc.level.entitiesForRendering()) {
                if (i instanceof RunicOverseer overseer) {
                    playMusic = true;
                    mus = SoundInit.RUNICOVERSEER.get();
                    if (overseer.getEntityData().get(RunicOverseer.INTRO)) {
                        mus = SoundInit.RUNICOVERSEERINTRO.get();
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
            }
            if (!manager.isActive(music) && playMusic)
            {
                music = SimpleSoundInstance.forMusic(mus);
                manager.play(music);
            }
        }
    }
}
