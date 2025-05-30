package com.cmdpro.runology.entity;

import com.cmdpro.runology.data.entries.Entry;
import com.cmdpro.runology.data.entries.EntryManager;
import com.cmdpro.runology.data.entries.EntryTab;
import com.cmdpro.runology.data.entries.EntryTabManager;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RunicCodex extends Entity {
    public AnimationState animState = new AnimationState();
    public HashMap<ResourceLocation, RunicCodexEntry> entryEntities = new HashMap<>();

    public RunicCodex(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public ServerPlayer owner;
    public RunicCodex(Level level, ServerPlayer owner) {
        this(EntityRegistry.RUNIC_CODEX.get(), level);
        this.owner = owner;
        updateUnlocked(owner);
    }
    public EntryTab tab;
    public List<EntryTab> avaliableTabs = new ArrayList<>();
    public List<Entry> avaliableEntries = new ArrayList<>();
    public void updateUnlocked(ServerPlayer owner) {
        avaliableTabs = EntryTabManager.tabs.values().stream().filter((i) -> i.isUnlocked(owner)).toList();
        avaliableEntries = EntryManager.entries.values().stream().filter((i) -> i.isUnlocked(owner)).toList();
        if (tab == null || !avaliableTabs.contains(tab)) {
            avaliableTabs.stream().findFirst().ifPresent(entryTab -> this.tab = entryTab);
        }
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
            i.remove(reason);
        }
        entryEntities.clear();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            boolean anyoneNearby = false;
            for (Player i : level().players()) {
                if (i.distanceTo(this) <= 15) {
                    anyoneNearby = true;
                }
            }
            if (!anyoneNearby) {
                for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
                    i.remove(RemovalReason.DISCARDED);
                }
                entryEntities.clear();
            } else if (entryEntities.isEmpty()) {
                for (Entry i : avaliableEntries) {
                    createEntryEntity(position().add(i.pos), i.id);
                }
                for (RunicCodexEntry i : entryEntities.values()) {
                    for (ResourceLocation j : i.getEntry().parents) {
                        RunicCodexEntry entryEntity = entryEntities.get(j);
                        if (entryEntity != null) {
                            i.parentEntities.add(entryEntity);
                        }
                    }
                    level().addFreshEntity(i);
                }
            }
        }
    }
    public RunicCodexEntry createEntryEntity(Vec3 position, ResourceLocation id) {
        RunicCodexEntry entity = new RunicCodexEntry(level(), id);
        entity.setPos(position);
        entity.codex = this;
        entity.entryDataDirty = true;
        entryEntities.put(id, entity);
        return entity;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide) {
            if (player.isShiftKeyDown()) {
                ItemEntity item = new ItemEntity(level(), position().x, position().y, position().z, new ItemStack(ItemRegistry.GUIDEBOOK.get()));
                level().addFreshEntity(item);
                remove(RemovalReason.DISCARDED);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
