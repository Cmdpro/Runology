package com.cmdpro.runology.block;

import com.cmdpro.runology.block.entity.RunicAnalyzerBlockEntity;
import com.cmdpro.runology.entity.RunicOverseer;
import com.cmdpro.runology.entity.Shatter;
import com.cmdpro.runology.entity.VoidBeam;
import com.cmdpro.runology.init.BlockEntityInit;
import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MysteriousAltar extends Block {
    public MysteriousAltar(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pPlayer.getItemInHand(pHand).is(ItemInit.MYSTERIUMTOTEM.get())) {
                if (pLevel.getEntitiesOfClass(RunicOverseer.class, AABB.ofSize(pPos.getCenter(), 50, 50, 50)).size() <= 0) {
                    pPlayer.getItemInHand(pHand).shrink(1);
                    RunicOverseer boss = new RunicOverseer(EntityInit.RUNICOVERSEER.get(), pLevel);
                    boss.setPos(pPos.getCenter().add(0, 1, 0));
                    boss.finalizeSpawn((ServerLevel) pLevel, pLevel.getCurrentDifficultyAt(pPos), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                    pLevel.addFreshEntity(boss);
                    for (Shatter i : pLevel.getEntitiesOfClass(Shatter.class, AABB.ofSize(pPos.getCenter().add(0, 1, 0), 5, 5, 5))) {
                        i.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            } else {
                pPlayer.sendSystemMessage(Component.translatable("block.runology.mysteriousaltar.seemsneedtotem").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
