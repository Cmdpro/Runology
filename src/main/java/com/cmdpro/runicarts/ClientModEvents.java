package com.cmdpro.runicarts;

import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.client.ModHud;
import com.cmdpro.runicarts.api.RunicArtsRegistration;
import com.cmdpro.runicarts.init.*;
import com.cmdpro.runicarts.integration.BookAltarRecipePage;
import com.cmdpro.runicarts.integration.BookAltarRecipePageRenderer;
import com.cmdpro.runicarts.integration.RunicArtsModonomiconConstants;
import com.cmdpro.runicarts.integration.bookconditions.BookAncientKnowledgeCondition;
import com.cmdpro.runicarts.integration.bookconditions.BookKnowledgeCondition;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.networking.ModMessages;
import com.cmdpro.runicarts.networking.packet.PlayerUnlockEntryC2SPacket;
import com.cmdpro.runicarts.particle.Soul2Particle;
import com.cmdpro.runicarts.particle.Soul3Particle;
import com.cmdpro.runicarts.particle.SoulParticle;
import com.cmdpro.runicarts.renderers.*;
import com.cmdpro.runicarts.screen.DivinationTableScreen;
import com.cmdpro.runicarts.screen.SoulShaperScreen;
import com.cmdpro.runicarts.screen.SoulcastersTableScreen;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookEntryParent;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.events.ModonomiconEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RunicArts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    public static final IGuiOverlay HUD = ModHud::drawHUD;
    @SubscribeEvent
    public static void renderHUD(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("mod_hud", HUD);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(EntityInit.SPELLPROJECTILE.get(), BillboardProjectileRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.SPIRITTANK.get(), SpiritTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.SOULPOINT.get(), SoulPointRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.SOULALTAR.get(), SoulAltarRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.DIVINATIONTABLE.get(), DivinationTableRenderer::new);
    }
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ItemColor soulcasterCrystalColor = (stack, tintIndex) -> {
            if (stack.getItem() instanceof ISoulcastersCrystal) {
                ISoulcastersCrystal crystal = (ISoulcastersCrystal) stack.getItem();
                return RunicArtsUtil.SOULCASTER_EFFECTS_REGISTRY.get().getValue(ResourceLocation.of(crystal.getId(), ':')).color.getRGB();
            }
            return 0xffffff;
        };
        for (Item i : RunicArtsUtil.SOULCASTER_CRYSTALS) {
            event.getItemColors().register(soulcasterCrystalColor, i);
        }
        ItemColor fullSoulCrystalColor = (stack, tintIndex) -> {
            if (stack.is(ItemInit.FULLSOULCRYSTAL.get())) {
                if (stack.hasTag()) {
                    if (stack.getTag().contains("entitytype")) {
                        EntityType entity = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.of(stack.getTag().getString("entitytype"), ':'));
                        if (entity != null) {
                            SpawnEggItem egg = ForgeSpawnEggItem.fromEntityType(entity);
                            if (egg != null) {
                                return egg.getColor(0);
                            }
                        }
                    }
                }
            }
            return 0xffffff;
        };
        event.getItemColors().register(fullSoulCrystalColor, ItemInit.FULLSOULCRYSTAL.get());
    }
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        ModonomiconEvents.client().onEntryClicked((e) -> {
            BookEntry entry = BookDataManager.get().getBook(e.getBookId()).getEntry(e.getEntryId());
            if (entry.getCondition() instanceof BookKnowledgeCondition condition) {
                if (ClientPlayerData.getPlayerKnowledge() >= condition.knowledge) {
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
            if (entry.getCondition() instanceof BookAncientKnowledgeCondition condition) {
                if (ClientPlayerData.getPlayerAncientKnowledge() >= condition.knowledge) {
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
        MenuScreens.register(MenuInit.SOULSHAPER_MENU.get(), SoulShaperScreen::new);
        MenuScreens.register(MenuInit.SOULCASTERSTABLE_MENU.get(), SoulcastersTableScreen::new);
        MenuScreens.register(MenuInit.DIVINATIONTABLE_MENU.get(), DivinationTableScreen::new);
        EntityRenderers.register(EntityInit.SOULKEEPER.get(), SoulKeeperRenderer::new);
        EntityRenderers.register(EntityInit.SOULRITUALCONTROLLER.get(), SoulRitualControllerRenderer::new);
        PageRendererRegistry.registerPageRenderer(RunicArtsModonomiconConstants.Page.ALTAR_RECIPE, p -> new BookAltarRecipePageRenderer((BookAltarRecipePage) p));
    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleInit.SOUL.get(),
                SoulParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleInit.SOUL2.get(),
                Soul2Particle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleInit.SOUL3.get(),
                Soul3Particle.Provider::new);
    }
}
