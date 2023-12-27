package com.cmdpro.runicarts.block;
import com.cmdpro.runicarts.block.entity.SpiritTankBlockEntity;
import com.cmdpro.runicarts.init.BlockEntityInit;
import com.cmdpro.runicarts.init.ItemInit;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SpiritTank extends BaseEntityBlock {
    public SpiritTank(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return ((SpiritTankBlockEntity)pLevel.getBlockEntity(pPos)).getAnalogOutputSignal();
    }
    /* BLOCK ENTITY */

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SpiritTankBlockEntity(pPos, pState);
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.SOULLINKER.get()) || pPlayer.getItemInHand(InteractionHand.OFF_HAND).is(ItemInit.SOULLINKER.get())) {
            return InteractionResult.FAIL;
        }
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof SpiritTankBlockEntity) {
                pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                    SpiritTankBlockEntity tank = (SpiritTankBlockEntity) entity;
                    if (pPlayer.isShiftKeyDown()) {
                        if (tank.getSouls() > 0 && data.getSouls() < PlayerModData.getMaxSouls(pPlayer)) {
                            tank.setSouls(tank.getSouls() - 1);
                            data.setSouls(data.getSouls()+1);
                        }
                    } else {
                        if (tank.getSouls() < tank.getMaxSouls() && data.getSouls() > 0) {
                            tank.setSouls(tank.getSouls() + 1);
                            data.setSouls(data.getSouls()-1);
                        }
                    }
                });
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityInit.SPIRITTANK.get(),
                SpiritTankBlockEntity::tick);
    }
}
