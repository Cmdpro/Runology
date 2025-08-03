package com.cmdpro.runology.item;

import com.cmdpro.runology.registry.SoundRegistry;
import com.cmdpro.runology.entity.ThrownOtherworldBombProjectile;
import com.cmdpro.runology.registry.SoundRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class OtherworldBombItem extends Item implements ProjectileItem {
    @Override
    public Projectile asProjectile(Level pLevel, Position pPos, ItemStack pStack, Direction pDirection) {
        return new ThrownOtherworldBombProjectile(pLevel, pPos.x(), pPos.y(), pPos.z());
    }

    @Override
    public DispenseConfig createDispenseConfig() {
        return DispenseConfig.builder()
                .uncertainty(DispenseConfig.DEFAULT.uncertainty() * 0.5F)
                .power(DispenseConfig.DEFAULT.power() * 1.25F)
                .build();
    }
    public OtherworldBombItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound((Player)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundRegistry.OTHERWORLD_BOMB_THROW.value(), SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            ThrownOtherworldBombProjectile bomb = new ThrownOtherworldBombProjectile(pPlayer, pLevel);
            bomb.setItem(itemstack);
            bomb.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(bomb);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        pPlayer.getCooldowns().addCooldown(this, 20);

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}
