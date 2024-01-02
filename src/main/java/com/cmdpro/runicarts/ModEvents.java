package com.cmdpro.runicarts;

import com.cmdpro.runicarts.init.*;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;
import top.theillusivec4.curios.api.CuriosCapability;

import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@Mod.EventBusSubscriber(modid = RunicArts.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerModDataProvider.PLAYER_MODDATA).isPresent()) {
                event.addCapability(new ResourceLocation(RunicArts.MOD_ID, "properties"), new PlayerModDataProvider());
            }
        }
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
    protected static void syncToPlayer(ServerPlayer player) {

    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {

                data.updateData(event.player);
            });
        }
    }
    @SubscribeEvent
    public static void entityStrikedByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof ItemEntity entity) {
            if (entity.getItem().is(ItemInit.FIRERUNE.get())) {
                entity.playSound(SoundEvents.BEACON_POWER_SELECT);
                entity.setInvulnerable(true);
                entity.setItem(new ItemStack(ItemInit.ENERGYRUNE.get(), entity.getItem().getCount(), entity.getItem().getTag()));
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.END_ROD, entity.position().x, entity.position().y, entity.position().z, 50, 0, 0, 0, 0.1);
            }
        }
    }
    @SubscribeEvent
    public static void cropGrowEvent(BlockEvent.CropGrowEvent.Post event) {
        if (!event.isCanceled()) {
            List<ItemEntity> entities = event.getLevel().getEntitiesOfClass(ItemEntity.class, AABB.ofSize(event.getPos().getCenter(), 2, 2, 2));
            for (ItemEntity i : entities) {
                if (i.getItem().is(ItemInit.EARTHRUNE.get())) {
                    i.playSound(SoundEvents.BEACON_POWER_SELECT);
                    i.setItem(new ItemStack(ItemInit.PLANTRUNE.get(), i.getItem().getCount(), i.getItem().getTag()));
                    ((ServerLevel)i.level()).sendParticles(ParticleTypes.END_ROD, i.position().x, i.position().y, i.position().z, 50, 0, 0, 0, 0.1);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        oldPlayer.revive();
        if(event.isWasDeath()) {
            oldPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerModData.class);
    }
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                    data.updateData((ServerPlayer)event.getEntity());
                });
            }
        }
    }
    public static boolean playerHasNeededEntry(ServerPlayer player, boolean mustRead, String entry) {
        ConcurrentMap<ResourceLocation, Set<ResourceLocation>> entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).unlockedEntries;
        if (mustRead) {
            entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).readEntries;
        }
        for (Map.Entry<ResourceLocation, Set<ResourceLocation>> i : entries.entrySet()) {
            for (ResourceLocation o : i.getValue()) {
                if (o.toString().equals(entry)) {
                    return true;
                }
            }
        }
        return false;
    }
}
