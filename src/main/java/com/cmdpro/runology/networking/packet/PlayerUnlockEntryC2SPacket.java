package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.integration.modonomicon.bookconditions.BookAnalyzeTaskCondition;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

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

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BookAnalyzeTaskCondition condition = RunologyUtil.getAnalyzeCondition(BookDataManager.get().getBook(book).getEntry(entry).getCondition());
            if (condition != null) {
                if (RunologyUtil.conditionAllTrueExceptAnalyze(BookDataManager.get().getBook(book).getEntry(entry), context.getSender())) {
                    if (context.getSender().getInventory().contains(new ItemStack(Items.PAPER, 1))) {
                        context.getSender().getInventory().clearOrCountMatchingItems((item) -> item.is(Items.PAPER), 1, context.getSender().inventoryMenu.getCraftSlots());
                        ItemStack stack = new ItemStack(ItemInit.RESEARCH.get(), 1);
                        stack.getOrCreateTag().putInt("progress", 0);
                        stack.getOrCreateTag().putString("entry", entry.toString());
                        stack.getOrCreateTag().putString("book", book.toString());
                        stack.getOrCreateTag().putBoolean("finished", false);
                        if (!context.getSender().getInventory().add(stack)) {
                            ItemEntity itementity = context.getSender().drop(stack, false);
                            if (itementity != null) {
                                itementity.setNoPickUpDelay();
                                itementity.setTarget(context.getSender().getUUID());
                            }
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}