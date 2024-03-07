package com.cmdpro.runology.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellCrystal extends Item {
    public ResourceLocation spellLocation;
    public Spell spell;
    public SpellCrystal(Properties pProperties, ResourceLocation spell) {
        super(pProperties);
        this.spellLocation = spell;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (getSpell().staffCastable()) {
            pTooltipComponents.add(Component.translatable("item.runology.spellcrystal.staffspell"));
        } else if (getSpell().gauntletCastable()) {
            pTooltipComponents.add(Component.translatable("item.runology.spellcrystal.gauntletspell"));
        } else {
            pTooltipComponents.add(Component.translatable("item.runology.spellcrystal.unknownspelltype"));
        }
        pTooltipComponents.add(Component.translatable("item.runology.spellcrystal.tier", getSpell().magicLevel()));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public Spell getSpell() {
        if (spell == null) {
            spell = RunologyUtil.SPELL_REGISTRY.get().getValue(spellLocation);
        }
        return spell;
    }
}
