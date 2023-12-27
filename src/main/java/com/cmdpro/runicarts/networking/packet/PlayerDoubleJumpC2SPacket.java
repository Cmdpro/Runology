package com.cmdpro.runicarts.networking.packet;

import com.cmdpro.runicarts.integration.bookconditions.BookAncientKnowledgeCondition;
import com.cmdpro.runicarts.integration.bookconditions.BookKnowledgeCondition;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PlayerDoubleJumpC2SPacket {

    public PlayerDoubleJumpC2SPacket() {

    }

    public PlayerDoubleJumpC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            context.getSender().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent((data) -> {
                data.setCanDoubleJump(false);
                if (data.getSouls() >= 2) {
                    data.setSouls(data.getSouls() - 2);
                }
                context.getSender().level().playSound(null, context.getSender().position().x, context.getSender().position().y, context.getSender().position().z, SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 2, 1);
            });
        });
        return true;
    }
}