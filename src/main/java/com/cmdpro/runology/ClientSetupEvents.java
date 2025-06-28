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
import com.cmdpro.runology.renderers.entity.RunicCodexRenderer;
import com.cmdpro.runology.shaders.PlayerPowerModeShader;
import com.cmdpro.runology.shaders.PlayerPowerShader;
import com.cmdpro.runology.shaders.ShatterShader;
import com.cmdpro.runology.shaders.SpecialBypassShader;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ClientHooks;
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
        playerPowerModeShader = new PlayerPowerModeShader();
        PostShaderManager.addShader(playerPowerModeShader);
        playerPowerModeShader.setActive(true);
        specialBypassShader = new SpecialBypassShader();
        PostShaderManager.addShader(specialBypassShader);
        specialBypassShader.setActive(true);
    }
    public static final Lazy<KeyMapping> BLINK_MAPPING = Lazy.of(() -> new KeyMapping("key.runology.blink", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.runology.runology"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(BLINK_MAPPING.get());
    }
    public static PostShaderInstance shatterShader;
    public static PostShaderInstance playerPowerShader;
    public static PostShaderInstance playerPowerModeShader;
    public static PostShaderInstance specialBypassShader;
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
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    }
}
