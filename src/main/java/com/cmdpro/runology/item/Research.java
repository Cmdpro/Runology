package com.cmdpro.runology.item;

import com.cmdpro.runology.api.RuneItem;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.moddata.PlayerModDataProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class Research extends Item {
    public Research(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.getItemInHand(pUsedHand).hasTag() && pPlayer.getItemInHand(pUsedHand).getTag().contains("finished") && pPlayer.getItemInHand(pUsedHand).getTag().getBoolean("finished") && pPlayer.getItemInHand(pUsedHand).getTag().contains("book") && pPlayer.getItemInHand(pUsedHand).getTag().contains("entry")) {
            if (!pLevel.isClientSide) {
                pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                    ResourceLocation book = ResourceLocation.tryParse(pPlayer.getItemInHand(pUsedHand).getTag().getString("book"));
                    ResourceLocation entry = ResourceLocation.tryParse(pPlayer.getItemInHand(pUsedHand).getTag().getString("entry"));
                    if (data.getUnlocked().containsKey(book)) {
                        if (!data.getUnlocked().get(book).contains(entry)) {
                            data.getUnlocked().get(book).add(entry);
                        }
                    } else {
                        ArrayList list = new ArrayList<>();
                        list.add(entry);
                        data.getUnlocked().put(book, list);
                    }
                });
                pPlayer.getItemInHand(pUsedHand).shrink(1);
            }
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
