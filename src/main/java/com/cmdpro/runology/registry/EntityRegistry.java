package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.*;
import com.cmdpro.runology.renderers.block.ShatterRenderer;
import com.cmdpro.runology.renderers.entity.EmptyEntityRenderer;
import com.cmdpro.runology.renderers.entity.RunicCodexEntryRenderer;
import com.cmdpro.runology.renderers.entity.RunicCodexRenderer;
import com.cmdpro.runology.renderers.entity.ShatterZapRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT, modid = Runology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Runology.MODID);
    public static final Supplier<EntityType<ShatterZap>> SHATTER_ZAP = register("shatter_zap", () -> EntityType.Builder.of((EntityType.EntityFactory<ShatterZap>) ShatterZap::new, MobCategory.MISC).noSave().noSummon().sized(0f, 0f).build(Runology.MODID + ":" + "shatter_zap"));
    public static final Supplier<EntityType<RunicCodex>> RUNIC_CODEX = register("runic_codex", () -> EntityType.Builder.of((EntityType.EntityFactory<RunicCodex>) RunicCodex::new, MobCategory.MISC).noSummon().sized(0.8f, 0.8f).build(Runology.MODID + ":" + "runic_codex"));
    public static final Supplier<EntityType<RunicCodexEntry>> RUNIC_CODEX_ENTRY = register("runic_codex_entry", () -> EntityType.Builder.of((EntityType.EntityFactory<RunicCodexEntry>) RunicCodexEntry::new, MobCategory.MISC).noSave().noSummon().sized(0.4f, 0.4f).build(Runology.MODID + ":" + "runic_codex_entry"));
    public static final Supplier<EntityType<ThrownOtherworldBombProjectile>> OTHERWORLD_BOMB = register("otherworld_bomb", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownOtherworldBombProjectile>) ThrownOtherworldBombProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).build(Runology.MODID + ":" + "otherworld_bomb"));
    private static <T extends EntityType<?>> Supplier<T> register(final String name, final Supplier<T> entity) {
        return ENTITY_TYPES.register(name, entity);
    }
    @SubscribeEvent
    public static void clientEntityRenderers(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegistry.SHATTER_ZAP.get(), ShatterZapRenderer::new);
        EntityRenderers.register(EntityRegistry.RUNIC_CODEX.get(), RunicCodexRenderer::new);
        EntityRenderers.register(EntityRegistry.RUNIC_CODEX_ENTRY.get(), RunicCodexEntryRenderer::new);
        EntityRenderers.register(EntityRegistry.OTHERWORLD_BOMB.get(), ThrownItemRenderer::new);
    }
}
