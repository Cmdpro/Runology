package com.cmdpro.runology.networking.packet;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.block.entity.RunicAnalyzerBlockEntity;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.integration.modonomicon.bookconditions.BookAnalyzeTaskCondition;
import com.klikli_dev.modonomicon.book.entries.BookEntry;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerClickAnalyzeButtonC2SPacket {
    private final BlockPos pos;

    public PlayerClickAnalyzeButtonC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public PlayerClickAnalyzeButtonC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (context.getSender().level().getBlockEntity(pos) instanceof RunicAnalyzerBlockEntity analyzer) {
                if (analyzer.item != null && analyzer.item.is(ItemInit.RESEARCH.get()) && analyzer.item.hasTag() && analyzer.item.getTag().contains("entry") && analyzer.item.getTag().contains("progress")) {
                    BookEntry entry = BookDataManager.get().getBook(new ResourceLocation(Runology.MOD_ID, "runologyguide")).getEntry(ResourceLocation.tryParse(analyzer.item.getTag().getString("entry")));
                    BookAnalyzeTaskCondition task = RunologyUtil.getAnalyzeCondition(entry.getCondition());
                    if (task != null) {
                        if (analyzer.item.getTag().getInt("progress") < task.tasks.length) {
                            if (task.tasks[analyzer.item.getTag().getInt("progress")].canComplete(context.getSender())) {
                                task.tasks[analyzer.item.getTag().getInt("progress")].onComplete(context.getSender());
                                analyzer.item.getTag().putInt("progress", analyzer.item.getTag().getInt("progress")+1);
                                if (analyzer.item.getTag().getInt("progress") >= task.tasks.length) {
                                    analyzer.item.getTag().putBoolean("finished", true);
                                }
                            }
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}