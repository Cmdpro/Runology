package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import com.klikli_dev.modonomicon.registry.ItemRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class Shatter extends Entity {
    public Shatter(EntityType<?> entityType, Level level) {
        super(entityType, level);
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
    @Override
    public void tick() {
        super.tick();
        List<ItemEntity> items = level().getEntitiesOfClass(ItemEntity.class, AABB.ofSize(position().add(0, 1, 0), 10, 10, 10));
        if (level().isClientSide) {
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                if (i.getItem().is(Items.BOOK)) {
                    Vec3 diff = i.position().add(0, 0.25, 0).subtract(position().add(0, 1, 0)).multiply(0.2f, 0.2f, 0.2f);
                    level().addParticle(ParticleRegistry.SHATTER.get(), position().x, position().y + 1, position().z, diff.x, diff.y, diff.z);
                }
            }
        } else {
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                if (i.getItem().is(Items.BOOK)) {
                    i.setData(AttachmentTypeRegistry.BOOK_CONVERSION_TIMER, i.getData(AttachmentTypeRegistry.BOOK_CONVERSION_TIMER)+1);
                    if (i.getData(AttachmentTypeRegistry.BOOK_CONVERSION_TIMER) >= 100) {
                        i.removeData(AttachmentTypeRegistry.BOOK_CONVERSION_TIMER);
                        i.getItem().shrink(1);
                        if (i.getItem().isEmpty()) {
                            i.remove(RemovalReason.KILLED);
                        }
                        ItemStack book = new ItemStack(ItemRegistry.MODONOMICON.get());
                        book.set(DataComponentRegistry.BOOK_ID, ResourceLocation.fromNamespaceAndPath(Runology.MODID, "guidebook"));
                        ItemEntity item = new ItemEntity(level(), i.position().x, i.position().y, i.position().z, book);
                        level().addFreshEntity(item);
                    }
                }
            }
        }
    }
}
