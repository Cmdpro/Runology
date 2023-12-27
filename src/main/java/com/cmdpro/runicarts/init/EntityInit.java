package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.spongepowered.asm.mixin.Shadow;

public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RunicArts.MOD_ID);
    public static final RegistryObject<EntityType<SpellProjectile>> SPELLPROJECTILE = ENTITY_TYPES.register("spell", () -> EntityType.Builder.of((EntityType.EntityFactory<SpellProjectile>) SpellProjectile::new, MobCategory.MISC).sized(0.25f, 0.25f).build(RunicArts.MOD_ID + ":" + "spell"));
    public static final RegistryObject<EntityType<SoulKeeper>> SOULKEEPER = ENTITY_TYPES.register("soulkeeper", () -> EntityType.Builder.of((EntityType.EntityFactory<SoulKeeper>) SoulKeeper::new, MobCategory.MONSTER).sized(0.75f, 1.8f).fireImmune().build(RunicArts.MOD_ID + ":" + "soulkeeper"));
    public static final RegistryObject<EntityType<SoulRitualController>> SOULRITUALCONTROLLER = ENTITY_TYPES.register("soulritualcontroller", () -> EntityType.Builder.of((EntityType.EntityFactory<SoulRitualController>) SoulRitualController::new, MobCategory.MISC).sized(0f, 0f).build(RunicArts.MOD_ID + ":" + "soulritualcontroller"));
}
