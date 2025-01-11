package com.cmdpro.runology.block.machines.shattercoil;

import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShatterCoilFiller extends Block {
    private static final VoxelShape SHAPE =  Block.box(3, 0, 3, 13, 8, 13);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    public ShatterCoilFiller(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (level.getBlockState(pos.below()).is(BlockRegistry.SHATTER_COIL.get())) {
            level.destroyBlock(pos.below(), true);
        }
    }
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!level.getBlockState(pos.below()).is(BlockRegistry.SHATTER_COIL.get())) {
            return false;
        }
        return super.canSurvive(state, level, pos);
    }
}
