package com.cmdpro.runology.block.machines.shattercoil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ShatterCoilBlockItem extends BlockItem {
    public ShatterCoilBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return context.getLevel().isUnobstructed(state, context.getClickedPos(), collisioncontext);
    }
}
