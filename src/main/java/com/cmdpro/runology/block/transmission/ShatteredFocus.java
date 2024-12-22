package com.cmdpro.runology.block.transmission;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof ShatteredFocusBlockEntity ent) {
            pLevel.getData(AttachmentTypeRegistry.SHATTERED_FOCUSES).remove(ent);
        }
        if (pLevel.getBlockEntity(pPos) instanceof ShatteredFocusBlockEntity ent) {
            for (BlockPos i : ent.connectedTo) {
                if (pLevel.getBlockEntity(i) instanceof ShatteredRelayBlockEntity ent2) {
                    ent2.connectedTo.remove(pPos);
                    ent2.updateBlock();
                }
            }
            ShatteredFlowNetwork.updatePaths(pLevel, ent.getBlockPos(), new ShatteredFlowNetwork(new ArrayList<>(), new ArrayList<>()), new ArrayList<>());
            ent.updateBlock();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.getBlockEntity(pos) instanceof ShatteredFocusBlockEntity ent) {
            for (ShatteredRelayBlockEntity i : level.getData(AttachmentTypeRegistry.SHATTERED_RELAYS)) {
                if (i.getBlockPos().getCenter().distanceTo(pos.getCenter()) <= 20) {
                    if (!i.connectedTo.contains(pos)) {
                        i.connectedTo.add(pos);
                        i.updateBlock();
                    }
                    if (!ent.connectedTo.contains(i.getBlockPos())) {
                        ent.connectedTo.add(i.getBlockPos());
                    }
                }
            }
            ShatteredFlowNetwork.updatePaths(level, ent.getBlockPos(), new ShatteredFlowNetwork(new ArrayList<>(), new ArrayList<>()), new ArrayList<>());
            ent.updateBlock();
        }
    }
}
