package com.cmdpro.runology.item;

import com.cmdpro.runology.api.RuneItem;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.integration.bookconditions.BookAnalyzeTaskCondition;
import com.cmdpro.runology.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookEntryParent;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.bookstate.BookVisualStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Research extends Item {
    public Research(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable(BookDataManager.get().getBook(ResourceLocation.tryParse(pStack.getTag().getString("book"))).getEntry(ResourceLocation.tryParse(pStack.getTag().getString("entry"))).getName()));
        if (pStack.getTag().getBoolean("finished")) {
            pTooltipComponents.add(Component.translatable("item.runology.research.complete"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.getItemInHand(pUsedHand).hasTag() && pPlayer.getItemInHand(pUsedHand).getTag().contains("finished") && pPlayer.getItemInHand(pUsedHand).getTag().getBoolean("finished") && pPlayer.getItemInHand(pUsedHand).getTag().contains("book") && pPlayer.getItemInHand(pUsedHand).getTag().contains("entry")) {
            if (!pLevel.isClientSide) {
                ResourceLocation book = ResourceLocation.tryParse(pPlayer.getItemInHand(pUsedHand).getTag().getString("book"));
                ResourceLocation entry = ResourceLocation.tryParse(pPlayer.getItemInHand(pUsedHand).getTag().getString("entry"));
                BookEntry bookEntry = BookDataManager.get().getBook(book).getEntry(entry);
                BookAnalyzeTaskCondition condition = RunologyUtil.getAnalyzeCondition(bookEntry.getCondition());
                if (condition != null) {
                    if (RunologyUtil.conditionAllTrueExceptAnalyze(bookEntry, pPlayer)) {
                        List<BookEntryParent> parents = BookDataManager.get().getBook(book).getEntry(entry).getParents();
                        boolean canSee = true;
                        for (BookEntryParent i : parents) {
                            if (!BookUnlockStateManager.get().isUnlockedFor(pPlayer, i.getEntry())) {
                                canSee = false;
                                break;
                            }
                        }
                        if (canSee) {
                            pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                                if (data.getUnlocked().containsKey(book)) {
                                    if (!data.getUnlocked().get(book).contains(entry)) {
                                        data.getUnlocked().get(book).add(entry);
                                        pPlayer.getItemInHand(pUsedHand).shrink(1);
                                        BookUnlockStateManager.get().updateAndSyncFor((ServerPlayer) pPlayer);
                                        pPlayer.sendSystemMessage(Component.translatable("item.runology.research.use", Component.translatable(bookEntry.getName())));
                                    } else {
                                        pPlayer.sendSystemMessage(Component.translatable("item.runology.research.alreadyhave", Component.translatable(bookEntry.getName())));
                                    }
                                } else {
                                    ArrayList list = new ArrayList<>();
                                    list.add(entry);
                                    data.getUnlocked().put(book, list);
                                    pPlayer.getItemInHand(pUsedHand).shrink(1);
                                    BookUnlockStateManager.get().updateAndSyncFor((ServerPlayer) pPlayer);
                                    pPlayer.sendSystemMessage(Component.translatable("item.runology.research.use", Component.translatable(bookEntry.getName())));
                                }
                            });
                        } else {
                            pPlayer.sendSystemMessage(Component.translatable("item.runology.research.previousentryneeded"));
                        }
                    } else {
                        pPlayer.sendSystemMessage(Component.translatable("item.runology.research.conditionsnotcompleted"));
                    }
                }
            }
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
