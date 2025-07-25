
package com.cmdpro.runology.item;

import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class ShatterExplarder extends Item {
    public ShatterExplarder(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(BlockRegistry.SHATTER.get())) {
            if (!context.getLevel().isClientSide && context.getPlayer() != null && context.getPlayer().canUseGameMasterBlocks()) {
                if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ShatterBlockEntity entity) {
                    entity.explard(context.getLevel(), context.getClickedPos());
                }
            }
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }
        return super.useOn(context);
    }
}
