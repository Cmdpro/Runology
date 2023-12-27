package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.api.ISoulContainer;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoulLinker extends Item {

    public SoulLinker(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(item, level, components, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide) {
            return InteractionResult.PASS;
        }
        BlockEntity entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (entity instanceof ISoulContainer) {
            ISoulContainer ent = (ISoulContainer)entity;
            pContext.getPlayer().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                if (pContext.getPlayer().isShiftKeyDown()) {
                    String text = pContext.getClickedPos().getX() + " " + pContext.getClickedPos().getY() + " " + pContext.getClickedPos().getZ();
                    pContext.getPlayer().sendSystemMessage(Component.translatable("item.runicarts.soullinker.remove", text));
                    data.setLinkingFrom(null);
                    ent.getLinked().clear();
                } else {
                    if (data.getLinkingFrom() == null) {
                        data.setLinkingFrom(pContext.getClickedPos());
                        String text = pContext.getClickedPos().getX() + " " + pContext.getClickedPos().getY() + " " + pContext.getClickedPos().getZ();
                        pContext.getPlayer().sendSystemMessage(Component.translatable("item.runicarts.soullinker.linkfrom", text));
                    } else if (!data.getLinkingFrom().equals(pContext.getClickedPos())) {
                        if (pContext.getLevel().getBlockEntity(data.getLinkingFrom()) instanceof ISoulContainer && pContext.getClickedPos().getCenter().distanceTo(data.getLinkingFrom().getCenter()) <= 10) {
                            List<BlockPos> map = ((ISoulContainer) pContext.getLevel().getBlockEntity(data.getLinkingFrom())).getLinked();
                            List<BlockPos> map2 = ent.getLinked();
                            if (map2.contains(data.getLinkingFrom())) {
                                map2.remove(data.getLinkingFrom());
                                map.remove(data.getLinkingFrom());
                            }
                            if (map.contains(pContext.getClickedPos())) {
                                map2.remove(data.getLinkingFrom());
                                map.remove(pContext.getClickedPos());
                            }
                            String text = pContext.getClickedPos().getX() + " " + pContext.getClickedPos().getY() + " " + pContext.getClickedPos().getZ();
                            String text2 = data.getLinkingFrom().getX() + " " + data.getLinkingFrom().getY() + " " + data.getLinkingFrom().getZ();
                            pContext.getPlayer().sendSystemMessage(Component.translatable("item.runicarts.soullinker.linkto", text, text2));
                            map.add(pContext.getClickedPos());
                            data.setLinkingFrom(null);
                        } else {
                            String text = pContext.getClickedPos().getX() + " " + pContext.getClickedPos().getY() + " " + pContext.getClickedPos().getZ();
                            String text2 = data.getLinkingFrom().getX() + " " + data.getLinkingFrom().getY() + " " + data.getLinkingFrom().getZ();
                            pContext.getPlayer().sendSystemMessage(Component.translatable("item.runicarts.soullinker.fail", text, text2));
                            data.setLinkingFrom(null);
                        }
                    }
                }
            });
            return InteractionResult.PASS;
        } else {
            pContext.getPlayer().sendSystemMessage(Component.translatable("item.runicarts.soullinker.cancel"));
            pContext.getPlayer().getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                data.setLinkingFrom(null);
            });
            return InteractionResult.PASS;
        }
    }
}
