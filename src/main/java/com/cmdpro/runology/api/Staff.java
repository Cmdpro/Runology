package com.cmdpro.runology.api;

import com.cmdpro.runology.init.SpellInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Staff extends Item {
    public int magicLevel;
    public Staff(Properties properties, int magicLevel) {
        super(properties);
        this.magicLevel = magicLevel;
    }
    public Spell getSpell(Level level, Player player, InteractionHand hand) {
        return null;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Spell spell = getSpell(pLevel, pPlayer, pUsedHand);
            if (spell != null) {
                spell.cast(pPlayer, true, false);
                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
