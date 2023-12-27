package com.cmdpro.runicarts;

import com.cmdpro.runicarts.api.*;
import com.cmdpro.runicarts.block.SpiritTank;
import com.cmdpro.runicarts.config.RunicArtsConfig;
import com.cmdpro.runicarts.entity.SoulKeeper;
import com.cmdpro.runicarts.entity.SoulRitualController;
import com.cmdpro.runicarts.init.*;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.cmdpro.runicarts.networking.ModMessages;
import com.cmdpro.runicarts.networking.packet.PlayerDoubleJumpC2SPacket;
import com.cmdpro.runicarts.networking.packet.PlayerUnlockEntryC2SPacket;
import com.cmdpro.runicarts.particle.Soul3Particle;
import com.cmdpro.runicarts.particle.Soul3ParticleOptions;
import com.klikli_dev.modonomicon.Modonomicon;
import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.api.datagen.MultiblockProvider;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.klikli_dev.modonomicon.multiblock.DenseMultiblock;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Math;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            player.getCapability(CuriosCapability.INVENTORY).ifPresent((data) -> {
                if (data.findFirstCurio(ItemInit.SOULBARRIER.get()).isPresent()) {
                    player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent((data2) -> {
                        if (data2.getSouls() > 0) {
                            event.setAmount(event.getAmount()*0.9f);
                            data2.setSouls(data2.getSouls()-1);
                        }
                    });
                }
            });
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
                if (event.player.isHolding(ItemInit.PURGATORYWAND.get()) && data.getSouls() < 5f) {
                    data.setSouls(data.getSouls()+0.1f);
                }
                if (data.getSouls() > PlayerModData.getMaxSouls(event.player)) {
                    data.setSouls(PlayerModData.getMaxSouls(event.player));
                }
                event.player.getCapability(CuriosCapability.INVENTORY).ifPresent((data2) -> {
                    if (data2.findFirstCurio(ItemInit.SOULBOOSTER.get()).isPresent()) {
                        if (event.player.onGround()) {
                            data.setCanDoubleJump(true);
                        }
                    } else {
                        data.setCanDoubleJump(false);
                    }
                });
                data.updateData(event.player);
            });
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
    @SubscribeEvent
    public static void onPlayerPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (event.getPlacedBlock().is(Blocks.SOUL_FIRE) && !event.getLevel().isClientSide()) {
            Multiblock ritual = MultiblockDataManager.get().getMultiblock(new ResourceLocation(RunicArts.MOD_ID, "soulritual"));
            if (
                    ritual.validate(event.getEntity().level(), event.getPos().below(), Rotation.NONE) ||
                    ritual.validate(event.getEntity().level(), event.getPos().below(), Rotation.CLOCKWISE_90) ||
                    ritual.validate(event.getEntity().level(), event.getPos().below(), Rotation.CLOCKWISE_180) ||
                    ritual.validate(event.getEntity().level(), event.getPos().below(), Rotation.COUNTERCLOCKWISE_90)
            ) {
                if (playerHasNeededEntry((ServerPlayer)event.getEntity(), true, "runicarts:arcane/soulritual")) {
                    List<SoulKeeper> entitiesNearby = event.getEntity().level().getEntitiesOfClass(SoulKeeper.class, AABB.ofSize(event.getPos().getCenter(), 50, 50, 50));
                    if (entitiesNearby.size() <= 0) {
                        SoulRitualController ritualController = new SoulRitualController(EntityInit.SOULRITUALCONTROLLER.get(), event.getEntity().level());
                        ritualController.setPos(event.getPos().getCenter());
                        event.getEntity().level().addFreshEntity(ritualController);
                    } else {
                        event.getEntity().sendSystemMessage(Component.translatable("object.runicarts.soulritualfail").withStyle(ChatFormatting.RED));
                    }
                } else {
                    event.getEntity().sendSystemMessage(Component.translatable("object.runicarts.soulritualfail2").withStyle(ChatFormatting.RED));
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof SoulKeeper soulKeeper) {
                for (Player i : soulKeeper.level().players()) {
                    if (soulKeeper.ritualPos.distanceTo(i.position()) <= 10) {
                        ModCriteriaTriggers.KILLSOULKEEPER.trigger((ServerPlayer)i);
                    }
                }
            }
            if (event.getSource().getEntity() instanceof Player player) {
                if (player.getInventory().contains(new ItemStack(ItemInit.EMPTYSOULGEM.get()))) {
                    int slot = 0;
                    for (ItemStack i : player.getInventory().items) {
                        if (i.is(ItemInit.EMPTYSOULGEM.get())) {
                            break;
                        }
                        slot++;
                    }
                    Item gem = ItemInit.EASYSOULGEM.get();
                    if (event.getEntity().level().random.nextBoolean()) {
                        gem = ItemInit.MEDIUMSOULGEM.get();
                        if (event.getEntity().level().random.nextBoolean()) {
                            gem = ItemInit.HARDSOULGEM.get();
                            if (event.getEntity().level().random.nextBoolean()) {
                                gem = ItemInit.INSANESOULGEM.get();
                            }
                        }
                    }
                    player.getInventory().setItem(slot, new ItemStack(gem));
                }
                player.getCapability(CuriosCapability.INVENTORY).ifPresent((data) -> {
                    if (player.getMainHandItem().is(ItemInit.SOULMETALDAGGER.get()) || player.getMainHandItem().is(ItemInit.PURGATORYDAGGER.get()) || data.findFirstCurio(ItemInit.SOULORB.get()).isPresent()) {
                        player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data2 -> {
                            float amount = Math.floor(event.getEntity().getMaxHealth() / 10) + 1;
                            if (player.getMainHandItem().is(ItemInit.PURGATORYDAGGER.get())) {
                                amount *= 2;
                            }
                            if (!data.findFirstCurio(ItemInit.SOULTRANSFORMER.get()).isPresent()) {
                                data2.setSouls(data2.getSouls() + amount);
                                if (data2.getSouls() >= PlayerModData.getMaxSouls(player)) {
                                    data2.setSouls(PlayerModData.getMaxSouls(player));
                                }
                            } else {
                                amount /= 4;
                                player.heal(amount);
                            }
                            Soul3ParticleOptions particle = new Soul3ParticleOptions(player.getUUID());
                            ((ServerLevel) event.getEntity().level()).sendParticles(particle, event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, (int) Math.floor(amount), 0.1, 0.1, 0.1, 0);
                        });
                    }
                });
            }
        }
    }
}
