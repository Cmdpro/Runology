package com.cmdpro.runology;

import com.cmdpro.runology.init.*;
import com.cmdpro.runology.integration.BookRunicRecipePage;
import com.cmdpro.runology.integration.BookRunicRecipePageRenderer;
import com.cmdpro.runology.integration.RunologyModonomiconConstants;
import com.cmdpro.runology.integration.bookconditions.BookAnalyzeTaskCondition;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.moddata.ClientPlayerData;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerUnlockEntryC2SPacket;
import com.cmdpro.runology.renderers.*;
import com.cmdpro.runology.screen.RunicAnalyzerScreen;
import com.cmdpro.runology.screen.RunicWorkbenchScreen;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookEntryParent;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.events.ModonomiconEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICWORKBENCH.get(), RunicWorkbenchRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.VOIDGLASS.get(), VoidGlassRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICANALYZER.get(), RunicAnalyzerRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        registerDimensionEffects((dimension, effects) -> event.register(dimension.location(), effects)); //
    }
    public static void registerDimensionEffects(BiConsumer<ResourceKey<Level>, ShatterRealmEffects> consumer) {
        consumer.accept(DimensionInit.SHATTERREALM, new ShatterRealmEffects());
    }
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        ModonomiconEvents.client().onEntryClicked((e) -> {
            BookEntry entry = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId());
            if (entry.getCondition() instanceof BookAnalyzeTaskCondition condition) {
                if (!BookUnlockStateManager.get().isUnlockedFor(Minecraft.getInstance().player, entry)) {
                    List<BookEntryParent> parents = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId()).getParents();
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
        });
        MenuScreens.register(MenuInit.RUNICWORKBENCHMENU.get(), RunicWorkbenchScreen::new);
        MenuScreens.register(MenuInit.RUNICANALYZERMENU.get(), RunicAnalyzerScreen::new);
        PageRendererRegistry.registerPageRenderer(RunologyModonomiconConstants.Page.RUNICRECIPE, p -> new BookRunicRecipePageRenderer((BookRunicRecipePage) p));
        EntityRenderers.register(EntityInit.RUNICCONSTRUCT.get(), RunicConstructRenderer::new);
        EntityRenderers.register(EntityInit.RUNICSCOUT.get(), RunicScoutRenderer::new);
        EntityRenderers.register(EntityInit.RUNICOVERSEER.get(), RunicOverseerRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBOMB.get(), VoidBombRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBULLET.get(), VoidBulletRenderer::new);
        EntityRenderers.register(EntityInit.VOIDBEAM.get(), VoidBeamRenderer::new);
        EntityRenderers.register(EntityInit.SHATTER.get(), ShatterRenderer::new);
        event.enqueueWork(new Runnable() {
            public void run() {
                ItemProperties.register(ItemInit.INSTABILITYRESONATOR.get(), new ResourceLocation(Runology.MOD_ID, "instability"), (stack, level, entity, seed) -> {
                    return level.dimension().equals(DimensionInit.SHATTERREALM) ? ChunkModData.MAX_INSTABILITY : ClientPlayerData.getPlayerChunkInstability();
                });
            }
        });
        event.enqueueWork(new Runnable() {
            public void run() {
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
            }
        });
    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    }
}
