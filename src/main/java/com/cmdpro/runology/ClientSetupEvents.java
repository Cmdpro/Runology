package com.cmdpro.runology;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import com.cmdpro.runology.block.machines.ShatteredInfuserBlockEntity;
import com.cmdpro.runology.particle.PlayerPowerParticle;
import com.cmdpro.runology.particle.PlayerPowerPunchParticle;
import com.cmdpro.runology.particle.ShatterParticle;
import com.cmdpro.runology.particle.SmallShatterParticle;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.renderers.block.*;
import com.cmdpro.runology.shaders.PlayerPowerShader;
import com.cmdpro.runology.shaders.ShatterShader;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = Runology.MODID)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event)
    {
        shatterShader = new ShatterShader();
        PostShaderManager.addShader(shatterShader);
        shatterShader.setActive(true);
        playerPowerShader = new PlayerPowerShader();
        PostShaderManager.addShader(playerPowerShader);
        playerPowerShader.setActive(true);
    }
    public static final Lazy<KeyMapping> BLINK_MAPPING = Lazy.of(() -> new KeyMapping("key.runology.blink", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.runology.runology"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(BLINK_MAPPING.get());
    }
    public static PostShaderInstance shatterShader;
    public static PostShaderInstance playerPowerShader;
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.SHATTER.get(),
                ShatterParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.SMALL_SHATTER.get(),
                SmallShatterParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.PLAYER_POWER.get(),
                PlayerPowerParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.PLAYER_POWER_PUNCH.get(),
                PlayerPowerPunchParticle.Provider::new);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.SHATTER.get(), ShatterRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SHATTERED_RELAY.get(), ShatteredRelayRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SHATTERED_FOCUS.get(), ShatteredFocusRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.GOLD_PILLAR.get(), GoldPillarRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SHATTERED_INFUSER.get(), ShatteredInfuserRenderer::new);
    }
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(ResourceLocation.withDefaultNamespace("camera_overlays"), ResourceLocation.fromNamespaceAndPath(Runology.MODID, "player_power_overlay"), (guiGraphics, deltaTracker) -> {
            if (Minecraft.getInstance().player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                float time = (Minecraft.getInstance().level.getGameTime()+deltaTracker.getGameTimeDeltaPartialTick(true));
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, Math.clamp(0.8f+((float)Math.sin(Math.toRadians(time*5))*0.1f), 0f, 1f));
                guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "textures/gui/player_power_overlay.png"), 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        });
    }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    }
}
