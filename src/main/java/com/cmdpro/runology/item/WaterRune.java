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
import net.minecraft.world.level.block.Blocks;

public class WaterRune extends RuneItem {
    public WaterRune(Properties pProperties, ResourceLocation runicEnergyType) {
        super(pProperties, runicEnergyType);
    }
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.level().isClientSide()) {
            if (entity.level().getBlockState(entity.blockPosition()).is(Blocks.POWDER_SNOW)) {
                entity.playSound(SoundEvents.BEACON_POWER_SELECT);
                entity.setItem(new ItemStack(ItemInit.ICERUNE.get(), stack.getCount(), stack.getTag()));
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.END_ROD, entity.position().x, entity.position().y, entity.position().z, 50, 0, 0, 0, 0.1);
            }
        }
        return super.onEntityItemUpdate(stack, entity);
    }
}
