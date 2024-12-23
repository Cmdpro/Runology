package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.recipe.ShatterImbuementRecipe;
import com.cmdpro.runology.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShatteredRelayBlockEntity extends BlockEntity {
    public ShatteredRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTERED_RELAY.get(), pos, state);
        connectedTo = new ArrayList<>();
    }
    public ShatteredFlowNetwork path;
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        ListTag list = new ListTag();
        for (BlockPos i : connectedTo) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        connectedTo.clear();
        if (tag.contains("link")) {
            ListTag list = (ListTag) tag.get("link");
            for (Tag i : list) {
                CompoundTag blockpos = (CompoundTag) i;
                connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for (ShatteredRelayBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS)) {
            if (i == this) {
                continue;
            }
            if (i.getBlockPos().getCenter().distanceTo(getBlockPos().getCenter()) <= 20) {
                if (!i.connectedTo.contains(getBlockPos())) {
                    i.connectedTo.add(getBlockPos());
                    i.updateBlock();
                }
                if (!this.connectedTo.contains(i.getBlockPos())) {
                    this.connectedTo.add(i.getBlockPos());
                }
            }
        }
        for (ShatteredFocusBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES)) {
            if (i.getBlockPos().getCenter().distanceTo(getBlockPos().getCenter()) <= 20) {
                if (!i.connectedTo.contains(getBlockPos())) {
                    i.connectedTo.add(getBlockPos());
                    i.updateBlock();
                }
                if (!this.connectedTo.contains(i.getBlockPos())) {
                    this.connectedTo.add(i.getBlockPos());
                }
            }
        }
        ShatteredFlowNetwork.updatePaths(level, getBlockPos(), new ShatteredFlowNetwork(new ArrayList<>()), new ArrayList<>());
        updateBlock();
    }

    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    public List<BlockPos> connectedTo;
    @Override
    public void setLevel(Level pLevel) {
        if (level != null) {
            level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS).remove(this);
        }
        super.setLevel(pLevel);
        if (!pLevel.getData(AttachmentTypeRegistry.SHATTERED_RELAYS).contains(this)) {
            pLevel.getData(AttachmentTypeRegistry.SHATTERED_RELAYS).add(this);
        }
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide) {
            if (isPowered) {
                pLevel.addParticle(ParticleRegistry.SHATTER.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1, (level.random.nextFloat() * 0.2) - 0.1);
            }
        } else {
            boolean powered = false;
            for (BlockPos i : path.starts) {
                if (level.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent) {
                    if (ent.storage.amount > 0) {
                        powered = true;
                        break;
                    }
                }
            }
            if (isPowered != powered) {
                updateBlock();
            }
            isPowered = powered;
        }
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (!pkt.getTag().isEmpty()) {
            decodeUpdateTag(pkt.getTag(), lookupProvider);
        }
    }
    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        decodeUpdateTag(tag, lookupProvider);
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ListTag list = (ListTag)tag.get("link");
        connectedTo.clear();
        for (Tag i : list) {
            CompoundTag blockpos = (CompoundTag)i;
            connectedTo.add(new BlockPos(blockpos.getInt("linkX"), blockpos.getInt("linkY"), blockpos.getInt("linkZ")));
        }
        isPowered = tag.getBoolean("isPowered");
    }
    public boolean isPowered;
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (BlockPos i : connectedTo) {
            CompoundTag blockpos = new CompoundTag();
            blockpos.putInt("linkX", i.getX());
            blockpos.putInt("linkY", i.getY());
            blockpos.putInt("linkZ", i.getZ());
            list.add(blockpos);
        }
        tag.put("link", list);
        tag.putBoolean("isPowered", isPowered);
        return tag;
    }
}
