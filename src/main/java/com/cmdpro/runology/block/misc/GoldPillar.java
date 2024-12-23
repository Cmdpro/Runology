package com.cmdpro.runology.block.misc;

import com.cmdpro.runology.api.shatteredflow.ShatteredFlowNetwork;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GoldPillar extends Block implements EntityBlock {
    private static final VoxelShape SHAPE =  Block.box(2, 0, 2, 14, 16, 14);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    public GoldPillar(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GoldPillarBlockEntity) {
                ((GoldPillarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof GoldPillarBlockEntity ent) {
                if (!ent.itemHandler.getStackInSlot(0).isEmpty()) {
                    pPlayer.getInventory().add(ent.itemHandler.getStackInSlot(0));
                    ent.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                }
                ent.updateBlock();
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof GoldPillarBlockEntity ent) {
                if (ent.itemHandler.getStackInSlot(0).isEmpty()) {
                    ent.itemHandler.setStackInSlot(0, pPlayer.getItemInHand(pHand).copy());
                    pPlayer.getItemInHand(pHand).shrink(pPlayer.getItemInHand(pHand).getCount());
                } else {
                    pPlayer.getInventory().add(ent.itemHandler.getStackInSlot(0));
                    ent.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                }
                ent.updateBlock();
            }
        }
        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GoldPillarBlockEntity(pPos, pState);
    }
}
