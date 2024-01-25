package com.cmdpro.runology.item;

import com.cmdpro.runology.entity.Shatter;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RealitySlicer extends SwordItem {
    public RealitySlicer(Properties pProperties) {
        super(ModTiers.REALITYSLICER, 1, -2.4F, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            HitResult result = pPlayer.pick(3, 4, false);
            if (pLevel.getEntitiesOfClass(Shatter.class, AABB.ofSize(result.getLocation(), 5, 5, 5)).size() <= 0) {
                Shatter shatter = new Shatter(EntityInit.SHATTER.get(), pLevel, true, 300);
                Vec3 pos = result.getLocation();
                if (!pLevel.getBlockState(BlockPos.containing(pos.add(0, -1, 0))).isSolid()) {
                    pos = pos.add(0, -1, 0);
                }
                shatter.getEntityData().set(Shatter.OPENED, true);
                shatter.setPos(pos);
                pLevel.addFreshEntity(shatter);
                pPlayer.getItemInHand(pUsedHand).hurtAndBreak(100, pPlayer, (e -> e.broadcastBreakEvent(pUsedHand)));
                shatter.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT);
                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
