package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.ShatterZap;
import com.cmdpro.runology.renderers.block.ShatterRenderer;
import com.cmdpro.runology.renderers.entity.ShatterZapRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
    public static final Supplier<EntityType<ShatterZap>> SHATTER_ZAP = register("shatter_zap", () -> EntityType.Builder.of((EntityType.EntityFactory<ShatterZap>) ShatterZap::new, MobCategory.MISC).sized(0f, 0f).build(Runology.MODID + ":" + "shatter_zap"));
    private static <T extends EntityType<?>> Supplier<T> register(final String name, final Supplier<T> entity) {
        return ENTITY_TYPES.register(name, entity);
    }
    @SubscribeEvent
    public static void clientEntityRenderers(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegistry.SHATTER_ZAP.get(), ShatterZapRenderer::new);
    }
}
