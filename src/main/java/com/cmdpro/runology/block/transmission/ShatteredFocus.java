package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.chunkloading.ChunkloadingEventHandler;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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

import java.util.ArrayList;
import java.util.List;

public class ShatteredFocus extends Block implements EntityBlock {
    private static final VoxelShape SHAPE =  Block.box(3, 0, 3, 13, 4, 13);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    public ShatteredFocus(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShatteredFocusBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ShatteredFocusBlockEntity ent) {
                ent.tick(level1, pos, state1);
            }
        };
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof ServerLevel serverLevel) {
            ChunkloadingEventHandler.shatterController.forceChunk(serverLevel, pos, level.getChunk(pos).getPos().x, level.getChunk(pos).getPos().z, false, true);
        }
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof ShatteredFocusBlockEntity ent) {
                pLevel.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).remove(ent);
            }
            if (pState.getBlock() != pNewState.getBlock()) {
                if (pLevel.getBlockEntity(pPos) instanceof ShatteredFocusBlockEntity ent) {
                    if (ent.path != null) {
                        ent.path.disconnectFromNetwork(pLevel, pPos);
                    }
                    for (BlockPos i : ent.connectedTo) {
                        if (pLevel.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                            ent2.connectedTo.remove(pPos);
                            ent2.updateBlock();
                        }
                    }
                    ent.connectedTo.clear();
                    ent.updateBlock();
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
