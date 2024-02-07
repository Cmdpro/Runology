package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.RunicEnergyType;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.spells.gauntlet.SummonTotemSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.function.Supplier;

public class SpellInit {
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "spells"), Runology.MOD_ID);

    public static final RegistryObject<Spell> SUMMONTOTEM = register("summontotem", () -> new SummonTotemSpell());
    private static <T extends Spell> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return SPELLS.register(name, item);
    }
}
