package com.cmdpro.runology.api;

import com.cmdpro.runology.entity.Shatter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EmpoweredGauntlet extends Gauntlet {
    public Item returnsTo;
    public EmpoweredGauntlet(Properties properties, int magicLevel, Item returnsTo) {
        super(properties, magicLevel);
        this.returnsTo = returnsTo;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        double entityReach = pPlayer.getEntityReach();
        if (pPlayer.pick(entityReach, 1, false) instanceof EntityHitResult result) {
            if (result.getEntity() instanceof Shatter) {
                return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
