package com.cmdpro.runology.entity;

import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.runology.data.entries.Entry;
import com.cmdpro.runology.data.entries.EntryManager;
import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class RunicCodexEntry extends Entity {
    public RunicCodex codex;
    public ResourceLocation id;
    public ItemStack icon;
    public List<RunicCodexEntry> parentEntities = new ArrayList<>();
    public List<Vec3> parentEntityLocations = new ArrayList<>();
    private Entry entry;
    public RunicCodexEntry(EntityType<?> entityType, Level level) {
        super(entityType, level);
        noCulling = true;
    }
    public RunicCodexEntry(Level level, ResourceLocation id) {
        this(EntityRegistry.RUNIC_CODEX_ENTRY.get(), level);
        this.id = id;
        icon = EntryManager.entries.get(id).icon;
    }
    public Entry getEntry() {
        if (entry == null) {
            entry = EntryManager.entries.get(id);
        }
        return entry;
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (codex != null) {
            codex.entryEntities.remove(this);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ENTRY_DATA.equals(key)) {
            parseEntryData(entityData.get(ENTRY_DATA));
        }
        super.onSyncedDataUpdated(key);
    }
    public boolean entryDataDirty;
    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            ClientHandler.spawnParticles(level(), getBoundingBox().getCenter());
        } else {
            parentEntityLocations.clear();
            for (var i : parentEntities) {
                parentEntityLocations.add(i.getBoundingBox().getCenter());
            }
            if (parentEntityLocations.isEmpty()) {
                if (codex != null) {
                    parentEntityLocations.add(codex.getBoundingBox().getCenter().add(0, -0.2, 0));
                }
            }
            if (entryDataDirty) {
                entityData.set(ENTRY_DATA, getEntryData());
                entryDataDirty = false;
            }
            if (codex != null) {
                if (((ServerLevel)level()).getEntity(codex.getUUID()) == null) {
                    remove(RemovalReason.DISCARDED);
                }
            } else {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide) {
            codex.openEntry(this);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    public CompoundTag getEntryData() {
        CompoundTag tag = new CompoundTag();
        ListTag entryPositions = new ListTag();
        for (Vec3 i : parentEntityLocations) {
            CompoundTag pos = new CompoundTag();
            pos.putDouble("x", i.x);
            pos.putDouble("y", i.y);
            pos.putDouble("z", i.z);
            entryPositions.add(pos);
        }
        tag.put("entryPositions", entryPositions);
        tag.putString("id", id.toString());
        return tag;
    }
    public void parseEntryData(CompoundTag tag) {
        parentEntityLocations.clear();
        ListTag entryPositions = (ListTag)tag.get("entryPositions");
        for (Tag i : entryPositions) {
            CompoundTag compound = (CompoundTag)i;
            parentEntityLocations.add(new Vec3(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z")));
        }
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
        if (id != this.id) {
            entry = EntryManager.entries.get(id);
        }
        this.id = id;
        icon = EntryManager.entries.get(id).icon;
    }
    public static final EntityDataAccessor<CompoundTag> ENTRY_DATA = SynchedEntityData.defineId(RunicCodexEntry.class, EntityDataSerializers.COMPOUND_TAG);
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ENTRY_DATA, new CompoundTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    private static class ClientHandler {
        public static void spawnParticles(Level level, Vec3 position) {
            if (Minecraft.getInstance().level == level) {
                for (int i = 0; i < 5; i++) {
                    Vec3 offset = new Vec3(
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.1f, 0.1f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.1f, 0.1f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.1f, 0.1f)
                    );
                    Vec3 speed = new Vec3(
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, 0.05f, 0.1f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f)
                    );
                    Minecraft.getInstance().particleEngine.createParticle(ParticleRegistry.SMALL_SHATTER.get(), position.x + offset.x, position.y + offset.y, position.z + offset.z, speed.x, speed.y, speed.z);
                }
                Vec3 speed = new Vec3(
                        Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                        Mth.nextFloat(Minecraft.getInstance().level.random, 0.05f, 0.1f),
                        Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f)
                );
                Minecraft.getInstance().particleEngine.createParticle(ParticleRegistry.SHATTER.get(), position.x, position.y, position.z, speed.x, speed.y, speed.z);
            }
        }
    }
}
