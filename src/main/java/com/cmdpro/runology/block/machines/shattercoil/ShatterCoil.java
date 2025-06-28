package com.cmdpro.runology.block.machines.shattercoil;

import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShatterCoil extends Block implements EntityBlock {
    private static final VoxelShape SHAPE =  Block.box(3, 0, 3, 13, 16, 13);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    public ShatterCoil(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        boolean isNew = pState.getBlock() != pNewState.getBlock();
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ShatterCoilBlockEntity ent) {
            ent.onRemove(pLevel, pPos, isNew);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ShatterCoilBlockEntity ent) {
                ent.tick(level1, pos, state1);
            }
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().getBlockState(context.getClickedPos().above()).canBeReplaced(context)) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!level.getBlockState(pos.above()).is(BlockRegistry.SHATTER_COIL_FILLER.get())) {
            return false;
        }
        return super.canSurvive(state, level, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShatterCoilBlockEntity(pPos, pState);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.getBlockState(pos.above()).canBeReplaced()) {
            level.setBlockAndUpdate(pos.above(), BlockRegistry.SHATTER_COIL_FILLER.get().defaultBlockState());
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof ServerLevel serverLevel) {
            if (level.getBlockState(pos.above()).is(BlockRegistry.SHATTER_COIL_FILLER.get())) {
                level.destroyBlock(pos.above(), true);
            }
        }
    }
}
