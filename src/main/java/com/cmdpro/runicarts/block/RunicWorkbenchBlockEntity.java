package com.cmdpro.runicarts.block;

import com.cmdpro.runicarts.init.BlockEntityInit;
import com.cmdpro.runicarts.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class RunicWorkbenchBlockEntity extends BaseEntityBlock {
    public RunicWorkbenchBlockEntity(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity(pPos, pState);
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity) {
                ((com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity) {
                if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.COPPERGAUNTLET.get()) || pPlayer.getItemInHand(InteractionHand.OFF_HAND).is(ItemInit.COPPERGAUNTLET.get())) {
                    return com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                }
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityInit.RUNICWORKBENCH.get(),
                com.cmdpro.runicarts.block.entity.RunicWorkbenchBlockEntity::tick);
    }
}
