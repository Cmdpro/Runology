package com.cmdpro.runology;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.*;
import com.cmdpro.runology.integration.modonomicon.*;
import com.cmdpro.runology.integration.modonomicon.bookconditions.*;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.moddata.ClientPlayerData;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerUnlockEntryC2SPacket;
import com.cmdpro.runology.postprocessors.EchoGogglesProcessor;
import com.cmdpro.runology.renderers.*;
import com.cmdpro.runology.screen.SpellTableScreen;
import com.cmdpro.runology.screen.RunicAnalyzerScreen;
import com.cmdpro.runology.screen.RunicWorkbenchScreen;
import com.klikli_dev.modonomicon.book.entries.BookEntry;
import com.klikli_dev.modonomicon.book.BookEntryParent;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.events.ModonomiconEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.systems.particle.type.LodestoneParticleType;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import java.util.List;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICWORKBENCH.get(), RunicWorkbenchRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.VOIDGLASS.get(), VoidGlassRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICANALYZER.get(), RunicAnalyzerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.SPELLTABLE.get(), SpellTableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICCAULDRON.get(), RunicCauldronRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.ENDERTRANSPORTER.get(), EnderTransporterRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        registerDimensionEffects((dimension, effects) -> event.register(dimension.location(), effects)); //
    }
    public static void registerDimensionEffects(BiConsumer<ResourceKey<Level>, ShatterRealmEffects> consumer) {
        consumer.accept(DimensionInit.SHATTERREALM, new ShatterRealmEffects());
    }
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String i : event.getSkins()) {
            var skin = event.getSkin(i);
            skin.addLayer(new CustomElytraLayer(skin, event.getEntityModels()));
        }
    }

    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        ModonomiconEvents.client().onEntryClicked((e) -> {
            BookEntry entry = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId());
            BookAnalyzeTaskCondition condition = RunologyUtil.getAnalyzeCondition(entry.getCondition());
            if (condition != null) {
                if (!BookUnlockStateManager.get().isUnlockedFor(Minecraft.getInstance().player, entry)) {
                    if (RunologyUtil.conditionAllTrueExceptAnalyze(entry, Minecraft.getInstance().player)) {
                        List<? extends BookEntryParent> parents = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId()).getParents();
                        boolean canSee = true;
                        for (BookEntryParent i : parents) {
                            if (!BookUnlockStateManager.get().isUnlockedFor(Minecraft.getInstance().player, i.getEntry())) {
                                canSee = false;
                                break;
                            }
                        }
                        if (canSee) {
                            ModMessages.sendToServer(new PlayerUnlockEntryC2SPacket(e.getEntryId(), e.getBookId()));
                        }
                    }
                }
            }
        });
        MenuScreens.register(MenuInit.RUNICWORKBENCHMENU.get(), RunicWorkbenchScreen::new);
        MenuScreens.register(MenuInit.RUNICANALYZERMENU.get(), RunicAnalyzerScreen::new);
        MenuScreens.register(MenuInit.SPELLTABLEMENU.get(), SpellTableScreen::new);
        PageRendererRegistry.registerPageRenderer(RunologyModonomiconConstants.Page.RUNICRECIPE, p -> new BookRunicRecipePageRenderer((BookRunicRecipePage) p));
        PageRendererRegistry.registerPageRenderer(RunologyModonomiconConstants.Page.RUNICCAULDRONITEM, p -> new BookRunicCauldronItemRecipePageRenderer((BookRunicCauldronItemRecipePage) p));
        PageRendererRegistry.registerPageRenderer(RunologyModonomiconConstants.Page.RUNICCAULDRONFLUID, p -> new BookRunicCauldronFluidRecipePageRenderer((BookRunicCauldronFluidRecipePage) p));
        EntityRenderers.register(EntityInit.RUNICCONSTRUCT.get(), RunicConstructRenderer::new);
        EntityRenderers.register(EntityInit.RUNICSCOUT.get(), RunicScoutRenderer::new);
        EntityRenderers.register(EntityInit.RUNICOVERSEER.get(), RunicOverseerRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBOMB.get(), VoidBombRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBULLET.get(), VoidBulletRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBEAM.get(), VoidBeamRenderer::new);
        EntityRenderers.register(EntityInit.SHATTER.get(), ShatterRenderer::new);
        EntityRenderers.register(EntityInit.PURITYARROW.get(), PurityArrowRenderer::new);
        EntityRenderers.register(EntityInit.TOTEM.get(), TotemRenderer::new);
        EntityRenderers.register(EntityInit.FIREBALL.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityInit.ICESHARD.get(), IceShardRenderer::new);
        EntityRenderers.register(EntityInit.SHATTERATTACK.get(), ShatterAttackRenderer::new);
        EntityRenderers.register(EntityInit.SPARKATTACK.get(), SparkAttackRenderer::new);
        EntityRenderers.register(EntityInit.PRISMATICBULLET.get(), EmptyEntityRenderer::new);
        event.enqueueWork(new Runnable() {
            public void run() {
                ItemProperties.register(ItemInit.INSTABILITYRESONATOR.get(), new ResourceLocation(Runology.MOD_ID, "instability"), (stack, level, entity, seed) -> {
                    if (level != null) {
                        return level.dimension().equals(DimensionInit.SHATTERREALM) ? ChunkModData.MAX_INSTABILITY : ClientPlayerData.getPlayerChunkInstability();
                    } else {
                        return ClientPlayerData.getPlayerChunkInstability();
                    }
                });
                ItemProperties.register(ItemInit.PRISMATICBLASTER.get(), new ResourceLocation(Runology.MOD_ID, "charge"), (stack, level, entity, seed) -> {
                    if (stack.hasTag()) {
                        if (stack.getTag().contains("charge")) {
                            return stack.getTag().getInt("charge");
                        }
                    }
                    return 0;
                });
                ItemProperties.register(ItemInit.RESEARCH.get(), new ResourceLocation(Runology.MOD_ID, "finished"), (stack, level, entity, seed) -> {
                    if (stack.hasTag()) {
                        if (stack.getTag().contains("finished")) {
                            if (stack.getTag().getBoolean("finished")) {
                                return 1;
                            }
                        }
                    }
                    return 0;
                });
                ItemProperties.register(ItemInit.DRAGONIUMELYTRA.get(), new ResourceLocation("broken"), (p_174590_, p_174591_, p_174592_, p_174593_) -> {
                            return ElytraItem.isFlyEnabled(p_174590_) ? 0.0F : 1.0F;
                        });
            }
        });
        echoGogglesProcessor = new EchoGogglesProcessor();
        PostProcessHandler.addInstance(echoGogglesProcessor);
    }
    public static PostProcessor echoGogglesProcessor;
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

    }
}
