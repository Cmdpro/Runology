package com.cmdpro.runology.block.world;

import com.cmdpro.runology.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShatterBlockEntity extends BlockEntity {
    public ShatterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTER.get(), pos, state);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
    }
    @Override
    public void setLevel(Level pLevel) {
        if (level != null) {
            level.getData(AttachmentTypeRegistry.SHATTERS).remove(this);
        }
        super.setLevel(pLevel);
        pLevel.getData(AttachmentTypeRegistry.SHATTERS).add(this);
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        Vec3 center = getBlockPos().getCenter();
        List<ItemEntity> items = pLevel.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(center, 10, 10, 10));
        if (pLevel.isClientSide) {
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                if (i.getItem().is(Items.BOOK)) {
                    Vec3 diff = i.position().add(0, 0.25, 0).subtract(center).multiply(0.2f, 0.2f, 0.2f);
                    pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
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
                            i.remove(Entity.RemovalReason.KILLED);
                        }
                        ItemStack book = new ItemStack(ItemRegistry.GUIDEBOOK.get());
                        ItemEntity item = new ItemEntity(pLevel, i.position().x, i.position().y, i.position().z, book);
                        pLevel.addFreshEntity(item);
                    }
                }
            }
            List<Player> players = pLevel.getEntitiesOfClass(Player.class, AABB.ofSize(center, 15, 15, 15));
            for (Player i : players) {
                CriteriaTriggerRegistry.FIND_SHATTER.get().trigger((ServerPlayer)i);
            }
        }
    }
}
