package com.cmdpro.runology.entity;

import com.cmdpro.runology.registry.EntityRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
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
import java.util.List;

public class RunicCodex extends Entity {
    public AnimationState animState = new AnimationState();
    public List<RunicCodexEntry> entryEntities = new ArrayList<>();

    public RunicCodex(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public RunicCodex(Level level) {
        super(EntityRegistry.RUNIC_CODEX.get(), level);
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        for (RunicCodexEntry i : entryEntities.stream().toList()) {
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
                for (RunicCodexEntry i : entryEntities.stream().toList()) {
                    i.remove(RemovalReason.DISCARDED);
                }
                entryEntities.clear();
            } else if (entryEntities.isEmpty()) {
                createEntryEntity(position().add(0, 2, 0));
                createEntryEntity(position().add(0, 4, 0));
                createEntryEntity(position().add(2, 3, 1));
                createEntryEntity(position().add(-4, 3, -1));
            }
        }
    }
    public RunicCodexEntry createEntryEntity(Vec3 position) {
        RunicCodexEntry entity = new RunicCodexEntry(level());
        entity.setPos(position);
        entity.codex = this;
        entryEntities.add(entity);
        level().addFreshEntity(entity);
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
