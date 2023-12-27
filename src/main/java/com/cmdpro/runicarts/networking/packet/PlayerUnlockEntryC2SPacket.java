package com.cmdpro.runicarts.networking.packet;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.integration.bookconditions.BookAncientKnowledgeCondition;
import com.cmdpro.runicarts.integration.bookconditions.BookKnowledgeCondition;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PlayerUnlockEntryC2SPacket {
    private final ResourceLocation entry;
    private final ResourceLocation book;

    public PlayerUnlockEntryC2SPacket(ResourceLocation entry, ResourceLocation book) {
        this.entry = entry;
        this.book = book;
    }

    public PlayerUnlockEntryC2SPacket(FriendlyByteBuf buf) {
        this.entry = buf.readResourceLocation();
        this.book = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(entry);
        buf.writeResourceLocation(book);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BookCondition condition = BookDataManager.get().getBook(book).getEntry(entry).getCondition();
            if (condition instanceof BookKnowledgeCondition condition2) {
                var advancement = context.getSender().getServer().getAdvancements().getAdvancement(condition2.advancementId);
                if (!condition2.hasAdvancement || (advancement != null && context.getSender().getAdvancements().getOrStartProgress(advancement).isDone())) {
                    context.getSender().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                        if (data.getKnowledge() >= condition2.knowledge) {
                            if (data.getUnlocked().containsKey(book)) {
                                if (!data.getUnlocked().get(book).contains(entry)) {
                                    data.getUnlocked().get(book).add(entry);
                                    data.setKnowledge(data.getKnowledge() - condition2.knowledge);
                                }
                            } else {
                                ArrayList list = new ArrayList<>();
                                list.add(entry);
                                data.getUnlocked().put(book, list);
                                data.setKnowledge(data.getKnowledge() - condition2.knowledge);
                            }
                            BookUnlockStateManager.get().updateAndSyncFor(context.getSender());
                        }
                    });
                }
            }
            if (condition instanceof BookAncientKnowledgeCondition condition2) {
                var advancement = context.getSender().getServer().getAdvancements().getAdvancement(condition2.advancementId);
                if (!condition2.hasAdvancement || (advancement != null && context.getSender().getAdvancements().getOrStartProgress(advancement).isDone())) {
                    context.getSender().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                        if (data.getAncientKnowledge() >= condition2.knowledge) {
                            if (data.getUnlocked().containsKey(book)) {
                                if (!data.getUnlocked().get(book).contains(entry)) {
                                    data.getUnlocked().get(book).add(entry);
                                    data.setAncientKnowledge(data.getAncientKnowledge() - condition2.knowledge);
                                }
                            } else {
                                ArrayList list = new ArrayList<>();
                                list.add(entry);
                                data.getUnlocked().put(book, list);
                                data.setAncientKnowledge(data.getAncientKnowledge() - condition2.knowledge);
                            }
                            BookUnlockStateManager.get().updateAndSyncFor(context.getSender());
                        }
                    });
                }
            }
        });
        return true;
    }
}