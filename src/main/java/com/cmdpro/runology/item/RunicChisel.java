package com.cmdpro.runology.item;

import com.cmdpro.runology.registry.DataComponentRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import com.cmdpro.runology.registry.ParticleRegistry;
import com.cmdpro.runology.rune.RuneChiselingResult;
import com.cmdpro.runology.rune.RuneChiselingResultManager;
import com.cmdpro.runology.rune.RuneType;
import com.cmdpro.runology.rune.RuneTypeManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RunicChisel extends Item {
    public RunicChisel(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        RuneType rune = RuneTypeManager.types.getOrDefault(stack.get(DataComponentRegistry.RUNE), null);
        if (rune != null) {
            tooltipComponents.add(rune.name.copy().withColor(rune.color.getRGB()));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                List<ResourceLocation> unlockedRunes = RuneTypeManager.types.entrySet().stream().filter((a) -> a.getValue().requiredAdvancement.isEmpty() || ((ServerPlayer)player).getAdvancements().getOrStartProgress(((ServerLevel)level).getServer().getAdvancements().get(a.getValue().requiredAdvancement.get())).isDone()).map(Map.Entry::getKey).toList();
                if (!unlockedRunes.isEmpty()) {
                    if (!unlockedRunes.contains(stack.get(DataComponentRegistry.RUNE))) {
                        stack.set(DataComponentRegistry.RUNE, unlockedRunes.getFirst());
                    } else {
                        stack.set(DataComponentRegistry.RUNE, unlockedRunes.get((unlockedRunes.indexOf(stack.get(DataComponentRegistry.RUNE))+1) % unlockedRunes.size()));
                    }
                    RuneType rune = RuneTypeManager.types.get(stack.get(DataComponentRegistry.RUNE));
                    player.displayClientMessage(rune.name.copy().withColor(rune.color.getRGB()), true);
                } else {
                    stack.remove(DataComponentRegistry.RUNE);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }
        if (!context.getLevel().isClientSide) {
            BlockState clickedBlock = context.getLevel().getBlockState(context.getClickedPos());
            Optional<RuneChiselingResult> result = RuneChiselingResultManager.types.values().stream().filter((a) -> clickedBlock.is(a.input) && a.rune.equals(context.getItemInHand().get(DataComponentRegistry.RUNE))).findFirst();
            if (result.isPresent()) {
                Vec3 center = context.getClickedPos().getCenter();
                ((ServerLevel)context.getLevel()).sendParticles(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, 100, 0.25f, 0.25f, 0.25f, 0.25f);
                BlockState state = result.get().output.defaultBlockState();
                for (Property i : clickedBlock.getProperties()) {
                    if (state.hasProperty(i)) {
                        state.setValue(i, clickedBlock.getValue(i));
                    }
                }
                context.getLevel().setBlock(context.getClickedPos(), state, Block.UPDATE_ALL);
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);
                context.getItemInHand().hurtAndBreak(1, (ServerLevel) context.getLevel(), (ServerPlayer) context.getPlayer(), (item) -> { context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND); });
            }
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(stack.getDamageValue() + 1);
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
}
