package com.cmdpro.runology;

import com.cmdpro.runology.commands.RunologyCommands;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerPowerModeSyncS2CPacket;
import com.cmdpro.runology.networking.packet.RuneTypeSyncS2CPacket;
import com.cmdpro.runology.networking.packet.StartFalseDeathS2CPacket;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.rune.RuneChiselingResultManager;
import com.cmdpro.runology.rune.RuneTypeManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Runology.MODID)
public class GameEvents {
    @SubscribeEvent
    public static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getAdvancement().id().equals(ResourceLocation.fromNamespaceAndPath(Runology.MODID, "shatter"))) {
            event.getEntity().sendSystemMessage(Component.translatable("modonomicon.runology.guidebook.discover").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(RuneTypeManager.getOrCreateInstance());
        event.addListener(RuneChiselingResultManager.getOrCreateInstance());
    }
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                syncToPlayer(player);
                ModMessages.sendToPlayersTrackingEntityAndSelf(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), player);
            }
        } else {
            syncToPlayer(event.getPlayer());
            ModMessages.sendToPlayer(new PlayerPowerModeSyncS2CPacket(event.getPlayer().getId(), event.getPlayer().getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), event.getPlayer());
        }
    }
    @SubscribeEvent
    public static void playerStartTracking(PlayerEvent.StartTracking evt) {
        Entity target = evt.getTarget();
        Player player = evt.getEntity();

        if (player instanceof ServerPlayer serverPlayer && target instanceof Player targetPlayer) {
            ModMessages.sendToPlayer(new PlayerPowerModeSyncS2CPacket(target.getId(), targetPlayer.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), serverPlayer);
        }
    }
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                ModMessages.sendToPlayer(new PlayerPowerModeSyncS2CPacket(player.getId(), player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)), player);
            }
        }
    }
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        RunologyCommands.register(event.getDispatcher());
    }
    protected static void syncToPlayer(ServerPlayer player) {
        ModMessages.sendToPlayer(new RuneTypeSyncS2CPacket(RuneTypeManager.types), player);
    }
    public static final ResourceLocation PLAYER_POWER_SPEED_UUID = ResourceLocation.fromNamespaceAndPath(Runology.MODID, "player_power_speed");
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().setData(AttachmentTypeRegistry.BLINK_COOLDOWN, Math.clamp(event.getEntity().getData(AttachmentTypeRegistry.BLINK_COOLDOWN)-1, 0, Integer.MAX_VALUE));
            if (event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                if (event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY) > 0) {
                    event.getEntity().heal(event.getEntity().getMaxHealth()/80f);
                }
                if (event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED).getModifier(PLAYER_POWER_SPEED_UUID) == null) {
                    event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(PLAYER_POWER_SPEED_UUID, 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }
                event.getEntity().setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, Math.clamp(event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY)-1, 0, Integer.MAX_VALUE));
            } else {
                event.getEntity().setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, 0);
                event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(PLAYER_POWER_SPEED_UUID);
            }
        } else {
            if (!event.getEntity().isLocalPlayer() || !ClientHandler.isFirstPerson()) {
                if (event.getEntity().getInventory().armor.stream().anyMatch((a) -> a.is(ItemRegistry.BLINK_BOOTS.get()))) {
                    List<Vec3> positions = new ArrayList<>();
                    positions.add(event.getEntity().position());
                    for (Vec3 i : positions) {
                        for (int j = 0; j < 10; j++) {
                            Vec3 pos = i.add(new Vec3(event.getEntity().getRandom().nextFloat() - 0.5f, 0, event.getEntity().getRandom().nextFloat() - 0.5f));
                            Vec3 velocity = new Vec3((event.getEntity().getRandom().nextFloat() - 0.5f)*0.2, event.getEntity().getRandom().nextFloat() * 0.2f, (event.getEntity().getRandom().nextFloat() - 0.5f)*0.2);
                            event.getEntity().level().addParticle(ParticleRegistry.SHATTER.get(), pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
                        }
                    }
                }
                if (event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                    List<Vec3> positions = new ArrayList<>();
                    positions.add(event.getEntity().position());
                    for (Vec3 i : positions) {
                        for (int j = 0; j < 25; j++) {
                            Vec3 pos = i.add(new Vec3(event.getEntity().getRandom().nextFloat() - 0.5f, 0, event.getEntity().getRandom().nextFloat() - 0.5f));
                            Vec3 velocity = new Vec3(event.getEntity().getRandom().nextFloat() - 0.5f, event.getEntity().getRandom().nextFloat() * 0.5f, event.getEntity().getRandom().nextFloat() - 0.5f);
                            event.getEntity().level().addParticle(ParticleRegistry.PLAYER_POWER.get(), pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getSource().getDirectEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                        Vec3 pos = event.getEntity().getBoundingBox().getCenter();
                        ((ServerLevel) event.getEntity().level()).sendParticles(ParticleRegistry.PLAYER_POWER_PUNCH.get(), pos.x, pos.y, pos.z, 50, 0, 0, 0, 0.25f);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY) <= 0) {
                    if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                        event.setNewDamage(event.getNewDamage() / 10f);
                    }
                }
            }
            if (event.getSource().getDirectEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                        event.setNewDamage(event.getNewDamage() + 10);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY) > 0) {
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                    event.setCanceled(true);
                    player.setHealth(1);
                    player.setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, 80);
                    ModMessages.sendToPlayer(new StartFalseDeathS2CPacket(event.getSource().getLocalizedDeathMessage(player), player.level().getLevelData().isHardcore()), (ServerPlayer)player);
                }
            }
        }
    }
    public static class ClientHandler {
        public static boolean isFirstPerson() {
            return Minecraft.getInstance().options.getCameraType().isFirstPerson();
        }
    }
}
