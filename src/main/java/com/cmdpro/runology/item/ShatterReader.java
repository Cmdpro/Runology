package com.cmdpro.runology.item;

import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ShatterReader extends Item {
    public ShatterReader(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(BlockRegistry.SHATTER.get())) {
            if (!context.getLevel().isClientSide) {
                if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ShatterBlockEntity entity) {
                    context.getPlayer().sendSystemMessage(Component.translatable("item.runology.shatter_reader.power", entity.getPower()));
                    context.getPlayer().sendSystemMessage(Component.translatable("item.runology.shatter_reader.stability", entity.getStability()));
                    context.getPlayer().sendSystemMessage(Component.translatable("item.runology.shatter_reader.output", entity.getOutputShatteredFlow()));
                }
            }
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }
        return super.useOn(context);
    }
}
