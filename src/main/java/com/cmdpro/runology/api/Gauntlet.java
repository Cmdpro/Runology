package com.cmdpro.runology.api;

import com.cmdpro.runology.init.SpellInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Gauntlet extends Item {
    public int magicLevel;
    public float maxRunicEnergy;
    public Gauntlet(Properties properties, int magicLevel, float maxRunicEnergy) {
        super(properties);
        this.magicLevel = magicLevel;
        this.maxRunicEnergy = maxRunicEnergy;
    }
    public Spell getSpell(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof SpellCrystal crystal) {
            Spell spell = crystal.getSpell();
            if (spell.gauntletCastable()) {
                return spell;
            }
        }
        return null;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Spell spell = getSpell(pLevel, pPlayer, pUsedHand);
            if (spell != null) {
                if (spell.gauntletCastable() && magicLevel >= spell.magicLevel()) {
                    spell.cast(pPlayer, false, true);
                    return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
