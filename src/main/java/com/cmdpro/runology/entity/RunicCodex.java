package com.cmdpro.runology.entity;

import com.cmdpro.databank.model.animation.DatabankAnimationReference;
import com.cmdpro.databank.model.animation.DatabankAnimationState;
import com.cmdpro.databank.model.animation.DatabankEntityAnimationState;
import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.runology.data.entries.Entry;
import com.cmdpro.runology.data.entries.EntryManager;
import com.cmdpro.runology.data.entries.EntryTab;
import com.cmdpro.runology.data.entries.EntryTabManager;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
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

import java.util.*;

public class RunicCodex extends Entity {
    public DatabankAnimationState animState = new DatabankEntityAnimationState("idle", this)
            .addAnim(new DatabankAnimationReference("idle", (state, definition) -> {}, (state, definition) -> {}))
            .addAnim(new DatabankAnimationReference("page_turn", (state, definition) -> {}, (state, definition) -> state.setAnim("idle")));
    public HashMap<ResourceLocation, RunicCodexEntry> entryEntities = new HashMap<>();

    public RunicCodex(EntityType<?> entityType, Level level) {
        super(entityType, level);
        animState.setLevel(level);
    }
    public ServerPlayer owner;
    public UUID ownerUUID;
    public RunicCodex(Level level, ServerPlayer owner) {
        this(EntityRegistry.RUNIC_CODEX.get(), level);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
        updateUnlocked(owner);
    }

    @Override
    protected void setLevel(Level level) {
        super.setLevel(level);
        animState.setLevel(level);
    }

    public boolean entryOpen;
    public WorldGuiEntity entryGui;
    public ResourceLocation tab;
    public List<ResourceLocation> avaliableTabs = new ArrayList<>();
    public List<ResourceLocation> avaliableEntries = new ArrayList<>();
    public void updateUnlocked(ServerPlayer owner) {
        for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
            i.remove(RemovalReason.DISCARDED);
        }
        Comparator<EntryTab> tabComparator = Comparator.comparing((i) -> i.priority);
        avaliableTabs = EntryTabManager.tabs.values().stream().filter((i) -> i.isUnlocked(owner)).sorted(tabComparator.reversed()).map((i) -> i.id).toList();
        avaliableEntries = EntryManager.entries.values().stream().filter((i) -> i.isUnlocked(owner)).map((i) -> i.id).toList();
        if (tab == null || !avaliableTabs.contains(tab)) {
            avaliableTabs.stream().findFirst().ifPresent(entryTab -> {
                this.tab = entryTab;
                syncData();
            });
        }
    }
    public void syncData() {
        entityData.set(DATA, createData());
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
        if (entryGui != null) {
            entryGui.remove(reason);
        }
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
            for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
                if (!getEntry(i.id).tab.equals(tab)) {
                    i.remove(RemovalReason.DISCARDED);
                    entryEntities.remove(i.id);
                }
            }
            if (!anyoneNearby) {
                for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
                    i.remove(RemovalReason.DISCARDED);
                }
                entryEntities.clear();
            } else if (entryEntities.isEmpty()) {
                if (entryGui == null && tab != null) {
                    if (ownerUUID != null && owner == null) {
                        Player player = level().getPlayerByUUID(ownerUUID);
                        if (player instanceof ServerPlayer serverPlayer) {
                            owner = serverPlayer;
                        }
                    }
                    if (owner != null) {
                        updateUnlocked(owner);
                    }
                    for (ResourceLocation i : avaliableEntries) {
                        Entry entry = getEntry(i);
                        if (entry != null) {
                            if (entry.tab.equals(tab)) {
                                createEntryEntity(position().add(0, 1f, 0).add(entry.pos), i);
                            }
                        }
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
    }
    public Entry getEntry(ResourceLocation id) {
        return EntryManager.entries.get(id);
    }
    public EntryTab getTab() {
        return EntryTabManager.tabs.get(tab);
    }
    public RunicCodexEntry createEntryEntity(Vec3 position, ResourceLocation id) {
        RunicCodexEntry entity = new RunicCodexEntry(level(), id);
        entity.setPos(position);
        entity.codex = this;
        entity.entryDataDirty = true;
        entryEntities.put(id, entity);
        return entity;
    }
    public void openEntry(RunicCodexEntry entry) {
        WorldGuiEntity worldGui = new WorldGuiEntity(level(), position().add(0, 2.25f, 0), WorldGuiRegistry.PAGE.get());
        level().addFreshEntity(worldGui);
        if (worldGui.gui instanceof PageWorldGui pageWorldGui) {
            pageWorldGui.pages = new ArrayList<>(entry.getEntry().pages);
            pageWorldGui.codex = this;
            pageWorldGui.entry = entry.id;
            worldGui.syncData();
        }
        entryGui = worldGui;
        for (RunicCodexEntry i : entryEntities.values().stream().toList()) {
            i.remove(RemovalReason.DISCARDED);
        }
        entryEntities.clear();
        entryOpen = true;
        syncData();
    }
    public void exitEntry() {
        entryGui.remove(RemovalReason.DISCARDED);
        entryGui = null;
        entryOpen = false;
        syncData();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide) {
            if (player.isShiftKeyDown()) {
                ItemEntity item = new ItemEntity(level(), position().x, position().y, position().z, new ItemStack(ItemRegistry.GUIDEBOOK.get()));
                level().addFreshEntity(item);
                remove(RemovalReason.DISCARDED);
            } else {
                if (entryGui == null) {
                    entityData.set(ANIMATION, "page_turn", true);
                    tab = avaliableTabs.get((avaliableTabs.indexOf(tab)+1) % avaliableTabs.size());
                    syncData();
                }
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }
    public CompoundTag createData() {
        CompoundTag tag = new CompoundTag();
        if (tab != null) {
            tag.putString("tab", tab.toString());
        }
        tag.putBoolean("entryOpen", entryOpen);
        return tag;
    }
    public void readData(CompoundTag data) {
        if (data.contains("tab")) {
            tab = ResourceLocation.tryParse(data.getString("tab"));
        }
        entryOpen = data.getBoolean("entryOpen");
    }
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(RunicCodex.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<CompoundTag> DATA = SynchedEntityData.defineId(RunicCodex.class, EntityDataSerializers.COMPOUND_TAG);
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ANIMATION, "idle");
        builder.define(DATA, new CompoundTag());
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ANIMATION.equals(key)) {
            animState.setAnim(entityData.get(ANIMATION));
            animState.resetAnim();
        }
        if (DATA.equals(key)) {
            readData(entityData.get(DATA));
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        avaliableEntries.clear();
        ListTag entries = (ListTag)compound.get("entries");
        for (Tag i : entries) {
            if (i instanceof CompoundTag tag) {
                ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
                avaliableEntries.add(id);
            }
        }
        avaliableTabs.clear();
        ListTag tabs = (ListTag)compound.get("tabs");
        for (Tag i : tabs) {
            if (i instanceof CompoundTag tag) {
                ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
                avaliableTabs.add(id);
            }
        }
        if (tab == null || !avaliableTabs.contains(tab)) {
            avaliableTabs.stream().findFirst().ifPresent(entryTab -> {
                this.tab = entryTab;
                syncData();
            });
        }
        if (compound.contains("owner")) {
            ownerUUID = compound.getUUID("owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        ListTag entries = new ListTag();
        for (ResourceLocation i : avaliableEntries) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", i.toString());
            entries.add(tag);
        }
        compound.put("entries", entries);
        ListTag tabs = new ListTag();
        for (ResourceLocation i : avaliableTabs) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", i.toString());
            tabs.add(tag);
        }
        compound.put("tabs", tabs);
        if (ownerUUID != null) {
            compound.putUUID("owner", ownerUUID);
        }
    }
}
