package com.cmdpro.runology;

import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import com.cmdpro.runology.block.machines.ShatteredInfuserBlockEntity;
import com.cmdpro.runology.particle.PlayerPowerParticle;
import com.cmdpro.runology.particle.PlayerPowerPunchParticle;
import com.cmdpro.runology.particle.ShatterParticle;
import com.cmdpro.runology.particle.SmallShatterParticle;
import com.cmdpro.runology.registry.BlockEntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.renderers.block.*;
import com.cmdpro.runology.shaders.PlayerPowerShader;
import com.cmdpro.runology.shaders.ShatterShader;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

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
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    }
}
