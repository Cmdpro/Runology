package com.cmdpro.runology.block.world;

import com.cmdpro.runology.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingShatteredCrystal extends Block {
    public static final MapCodec<BuddingAmethystBlock> CODEC = simpleCodec(BuddingAmethystBlock::new);
    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public MapCodec<BuddingAmethystBlock> codec() {
        return CODEC;
    }

    public BuddingShatteredCrystal(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = BlockRegistry.SMALL_SHATTERED_CRYSTAL_BUD.get();
            } else if (blockstate.is(BlockRegistry.SMALL_SHATTERED_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = BlockRegistry.MEDIUM_SHATTERED_CRYSTAL_BUD.get();
            } else if (blockstate.is(BlockRegistry.MEDIUM_SHATTERED_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = BlockRegistry.LARGE_SHATTERED_CRYSTAL_BUD.get();
            } else if (blockstate.is(BlockRegistry.LARGE_SHATTERED_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = BlockRegistry.SHATTERED_CRYSTAL_CLUSTER.get();
            }

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState()
                        .setValue(AmethystClusterBlock.FACING, direction)
                        .setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                level.setBlockAndUpdate(blockpos, blockstate1);
            }
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}
