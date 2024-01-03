package com.cmdpro.runology.item;

import com.cmdpro.runology.api.RuneItem;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class AirRune extends RuneItem {
    public AirRune(Properties pProperties, ResourceLocation runicEnergyType) {
        super(pProperties, runicEnergyType);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.level().dimension().equals(Level.END) && !entity.level().isClientSide()) {
            int value = 0;
            if (entity.getPersistentData().contains("endConvertTime")) {
                value = entity.getPersistentData().getInt("endConvertTime");
            }
            if (value != 50 && (float)value/5f == Math.round((float)value/5f)) {
                entity.level().playSound(null, BlockPos.containing(entity.position()), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1f, 1f);
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.DRAGON_BREATH, entity.position().x, entity.position().y, entity.position().z, 50, 0, 0, 0, 0.1);
            }
            value += 1;
            entity.getPersistentData().putInt("endConvertTime", value);
            if (value >= 50) {
                entity.playSound(SoundEvents.BEACON_POWER_SELECT);
                entity.setItem(new ItemStack(ItemInit.VOIDRUNE.get(), stack.getCount(), stack.getTag()));
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.END_ROD, entity.position().x, entity.position().y, entity.position().z, 50, 0, 0, 0, 0.1);
            }
        }
        return super.onEntityItemUpdate(stack, entity);
    }
}
