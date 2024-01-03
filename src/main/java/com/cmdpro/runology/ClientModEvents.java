package com.cmdpro.runology;

import com.cmdpro.runology.init.BlockEntityInit;
import com.cmdpro.runology.init.MenuInit;
import com.cmdpro.runology.integration.bookconditions.BookRunicKnowledgeCondition;
import com.cmdpro.runology.moddata.ClientPlayerData;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerUnlockEntryC2SPacket;
import com.cmdpro.runology.renderers.RunicWorkbenchRenderer;
import com.cmdpro.runology.screen.RunicWorkbenchMenu;
import com.cmdpro.runology.screen.RunicWorkbenchScreen;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookEntryParent;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.events.ModonomiconEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.RUNICWORKBENCH.get(), RunicWorkbenchRenderer::new);
    }
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        ModonomiconEvents.client().onEntryClicked((e) -> {
            BookEntry entry = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId());
            if (entry.getCondition() instanceof BookRunicKnowledgeCondition condition) {
                if (ClientPlayerData.getPlayerRunicKnowledge() >= condition.runicKnowledge) {
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
            }
        });
        MenuScreens.register(MenuInit.RUNICWORKBENCHMENU.get(), RunicWorkbenchScreen::new);
    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    }
}
