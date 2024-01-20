package com.cmdpro.runology.moddata;

import com.cmdpro.runology.init.AttributeInit;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.PlayerDataSyncS2CPacket;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerModData {
    public PlayerModData() {
        unlocked = new HashMap<>();
    }
    private HashMap<ResourceLocation, List<ResourceLocation>> unlocked;
    private int instabilityEventCooldown;
    public HashMap<ResourceLocation, List<ResourceLocation>> getUnlocked() {
        return unlocked;
    }
    public int getInstabilityEventCooldown() {
        return instabilityEventCooldown;
    }
    public void setInstabilityEventCooldown(int amount) {
        this.instabilityEventCooldown = amount;
    }

    public void updateData(ServerPlayer player) {
        if (player.level().getChunkAt(player.blockPosition()).getCapability(ChunkModDataProvider.CHUNK_MODDATA).isPresent()) {
            player.level().getChunkAt(player.blockPosition()).getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent((data) -> {
                ModMessages.sendToPlayer(new PlayerDataSyncS2CPacket(data.getInstability()), (player));
            });
        } else {
            ModMessages.sendToPlayer(new PlayerDataSyncS2CPacket(0), (player));
        }
    }
    public void updateData(Player player) {
        updateData((ServerPlayer)player);
    }
    public void copyFrom(PlayerModData source) {
        this.unlocked = source.unlocked;
    }
    public void saveNBTData(CompoundTag nbt) {
        if (!unlocked.isEmpty()) {
            ListTag tag = new ListTag();
            for (ResourceLocation i : unlocked.keySet()) {
                CompoundTag tag2 = new CompoundTag();
                tag2.putString("key", i.toString());
                ListTag tag3 = new ListTag();
                for (ResourceLocation o : unlocked.get(i)) {
                    CompoundTag tag4 = new CompoundTag();
                    tag4.putString("value", o.toString());
                    tag3.add(tag4);
                }
                tag2.put("value", tag3);
                tag.add(tag2);
            }
            nbt.put("unlocked", tag);
        }
    }
    public void loadNBTData(CompoundTag nbt) {
        unlocked.clear();
        if (nbt.contains("unlocked")) {
            for (Tag i : (ListTag) nbt.get("unlocked")) {
                List<ResourceLocation> list = new ArrayList<>();
                for (Tag o : (ListTag) ((CompoundTag) i).get("value")) {
                    list.add(ResourceLocation.of(((CompoundTag) o).getString("value"), ':'));
                }
                ResourceLocation id = ResourceLocation.of(((CompoundTag) i).getString("key"), ':');
                unlocked.put(id, list);
            }
        }
    }
}
