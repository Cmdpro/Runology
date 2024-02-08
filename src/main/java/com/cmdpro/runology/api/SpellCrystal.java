package com.cmdpro.runology.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SpellCrystal extends Item {
    public ResourceLocation spell;
    public SpellCrystal(Properties pProperties, ResourceLocation spell) {
        super(pProperties);
        this.spell = spell;
    }

    public Spell getSpell() {
        return RunologyUtil.SPELL_REGISTRY.get().getValue(spell);
    }
}
