package com.cmdpro.runology.block;

import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.TagInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PrismaticCrystal extends Block {
    public PrismaticCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (!pPlayer.getMainHandItem().is(TagInit.Items.REALITYBREAKERS)) {
            return 0f;
        }
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }
}
