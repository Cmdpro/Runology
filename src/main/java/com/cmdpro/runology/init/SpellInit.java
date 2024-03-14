package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.Spell;
import com.cmdpro.runology.spells.gauntlet.ConjureSparkSpell;
import com.cmdpro.runology.spells.gauntlet.SummonTotemSpell;
import com.cmdpro.runology.spells.staff.FireballSpell;
import com.cmdpro.runology.spells.staff.IceShardsSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SpellInit {
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "spells"), Runology.MOD_ID);

    public static final RegistryObject<Spell> SUMMONTOTEM = register("summontotem", () -> new SummonTotemSpell());
    public static final RegistryObject<Spell> FIREBALL = register("fireball", () -> new FireballSpell());
    public static final RegistryObject<Spell> ICESHARDS = register("iceshards", () -> new IceShardsSpell());
    public static final RegistryObject<Spell> CONJURESPARK = register("conjurespark", () -> new ConjureSparkSpell());
    private static <T extends Spell> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return SPELLS.register(name, item);
    }
}
