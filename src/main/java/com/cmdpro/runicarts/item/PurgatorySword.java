package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.init.AttributeInit;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;
import java.util.UUID;

public class PurgatorySword extends SwordItem {
    public PurgatorySword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.randomUUID();
        for (Map.Entry<Attribute, AttributeModifier> i : super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).entries()) {
            builder.put(i.getKey(), i.getValue());
        }
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(uuid, "Reach modifier", 1d, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
}