package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShatteredRelay extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected final VoxelShape northAabb;
    protected final VoxelShape southAabb;
    protected final VoxelShape eastAabb;
    protected final VoxelShape westAabb;
    protected final VoxelShape upAabb;
    protected final VoxelShape downAabb;


    public ShatteredRelay(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
        int pOffset = 4;
        int pSize = 8;
        this.upAabb = Block.box((double)pOffset, 0.0D, (double)pOffset, (double)(16 - pOffset), (double)pSize, (double)(16 - pOffset));
        this.downAabb = Block.box((double)pOffset, (double)(16 - pSize), (double)pOffset, (double)(16 - pOffset), 16.0D, (double)(16 - pOffset));
        this.northAabb = Block.box((double)pOffset, (double)pOffset, (double)(16 - pSize), (double)(16 - pOffset), (double)(16 - pOffset), 16.0D);
        this.southAabb = Block.box((double)pOffset, (double)pOffset, 0.0D, (double)(16 - pOffset), (double)(16 - pOffset), (double)pSize);
        this.eastAabb = Block.box(0.0D, (double)pOffset, (double)pOffset, (double)pSize, (double)(16 - pOffset), (double)(16 - pOffset));
        this.westAabb = Block.box((double)(16 - pSize), (double)pOffset, (double)pOffset, 16.0D, (double)(16 - pOffset), (double)(16 - pOffset));
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        switch (direction) {
            case NORTH:
                return this.northAabb;
            case SOUTH:
                return this.southAabb;
            case EAST:
                return this.eastAabb;
            case WEST:
                return this.westAabb;
            case DOWN:
                return this.downAabb;
            case UP:
            default:
                return this.upAabb;
        }
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, direction, SupportType.CENTER);
    }
    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        return pDirection == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace());
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof ShatteredRelayBlockEntity ent) {
                pLevel.getData(AttachmentTypeRegistry.SHATTERED_FLOW_CONNECTABLES).remove(ent);
            }
            if (pState.getBlock() != pNewState.getBlock()) {
                if (pLevel.getBlockEntity(pPos) instanceof ShatteredRelayBlockEntity ent) {
                    if (ent.path != null) {
                        ent.path.disconnectFromNetwork(pLevel, pPos);
                    }
                    for (BlockPos i : ent.connectedTo.stream().toList()) {
                        if (pLevel.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                            ent2.connectedTo.remove(pPos);
                            ent2.updateBlock();
                        }
                        if (pLevel.getBlockEntity(i) instanceof ShatteredFocusBlockEntity ent2) {
                            ent2.connectedTo.remove(pPos);
                            ent2.updateBlock();
                        }
                    }
                    ent.connectedTo.clear();
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ShatteredRelayBlockEntity(pPos, pState);
    }
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ShatteredRelayBlockEntity ent) {
                ent.tick(level1, pos, state1);
            }
        };
    }
}
