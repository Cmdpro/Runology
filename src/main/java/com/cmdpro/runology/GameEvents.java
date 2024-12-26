package com.cmdpro.runology;

import com.cmdpro.runology.commands.RunologyCommands;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.RuneTypeSyncS2CPacket;
import com.cmdpro.runology.networking.packet.StartFalseDeathS2CPacket;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.rune.RuneChiselingResultManager;
import com.cmdpro.runology.rune.RuneTypeManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
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
            }
        } else {
            syncToPlayer(event.getPlayer());
        }
    }
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        RunologyCommands.register(event.getDispatcher());
    }
    protected static void syncToPlayer(ServerPlayer player) {
        ModMessages.sendToPlayer(new RuneTypeSyncS2CPacket(RuneTypeManager.types), player);
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                List<Vec3> positions = new ArrayList<>();
                positions.add(event.getEntity().position());
                for (Vec3 i : positions) {
                    ((ServerLevel) event.getEntity().level()).sendParticles(ParticleRegistry.PLAYER_POWER.get(), i.x, i.y, i.z, 25, 0.1, 0.1, 0.1, 0.5f);
                }
                event.getEntity().setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, Math.clamp(event.getEntity().getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY)-1, 0, Integer.MAX_VALUE));
            } else {
                event.getEntity().setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, 0);
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
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY) > 0) {
                    event.setNewDamage(0);
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
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof Player player) {
                if (player.getData(AttachmentTypeRegistry.PLAYER_POWER_MODE)) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth());
                    player.setData(AttachmentTypeRegistry.PLAYER_POWER_INVINCIBILITY, 120);
                    ModMessages.sendToPlayer(new StartFalseDeathS2CPacket(event.getSource().getLocalizedDeathMessage(player), player.level().getLevelData().isHardcore()), (ServerPlayer)player);
                }
            }
        }
    }
}
