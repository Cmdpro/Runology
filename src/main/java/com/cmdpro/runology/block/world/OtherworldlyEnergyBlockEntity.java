package com.cmdpro.runology.block.world;

import com.cmdpro.runology.recipe.OtherworldlyEnergyRecipe;
import com.cmdpro.runology.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class OtherworldlyEnergyBlockEntity extends BlockEntity {
    public OtherworldlyEnergyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OTHERWORLDLY_ENERGY.get(), pos, state);
    }
    public int time;
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("time", time);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        time = tag.getInt("time");
    }

    public static final int MAX_TIME = 60*20;
    public ItemInteractionResult useItemOn(ItemStack usedStack, BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack stack = usedStack.copy();
            SingleRecipeInput input = new SingleRecipeInput(stack);
            Optional<RecipeHolder<OtherworldlyEnergyRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.OTHERWORLDLY_ENERGY_TYPE.get(), input, level);
            if (recipe.isPresent()) {
                RecipeHolder<OtherworldlyEnergyRecipe> holder = recipe.get();
                stack.shrink(1);
                player.setItemInHand(hand, stack);
                ItemStack output = holder.value().assemble(input, level.registryAccess());
                Vec3 vel = Vec3.directionFromRotation(level.getRandom().nextIntBetweenInclusive(-75, -45), level.getRandom().nextIntBetweenInclusive(0, 360)).scale(0.375f);
                Vec3 pos = blockPos.getCenter();
                ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, output, vel.x, vel.y, vel.z);
                level.addFreshEntity(entity);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide) {
            ClientHandler.spawnParticles(pLevel, pPos.getCenter());
        } else {
            AABB itemDetectionAABB = AABB.ofSize(pPos.getCenter(), 0.75, 0.75, 0.75);
            for (ItemEntity i : pLevel.getEntitiesOfClass(ItemEntity.class, itemDetectionAABB)) {
                ItemStack stack = i.getItem();
                SingleRecipeInput input = new SingleRecipeInput(stack);
                Optional<RecipeHolder<OtherworldlyEnergyRecipe>> recipe = pLevel.getRecipeManager().getRecipeFor(RecipeRegistry.OTHERWORLDLY_ENERGY_TYPE.get(), input, pLevel);
                if (recipe.isPresent()) {
                    RecipeHolder<OtherworldlyEnergyRecipe> holder = recipe.get();
                    ItemStack output = holder.value().assemble(input, pLevel.registryAccess());
                    int count = output.getCount()*stack.getCount();
                    Vec3 pos = i.position();
                    Vec3 vel = i.getDeltaMovement();
                    while (count > 64) {
                        ItemStack stack2 = output.copy();
                        stack2.setCount(64);
                        ItemEntity entity = new ItemEntity(pLevel, pos.x, pos.y, pos.z, stack2, vel.x, vel.y, vel.z);
                        pLevel.addFreshEntity(entity);
                        count -= 64;
                    }
                    output.setCount(count);
                    ItemEntity entity = new ItemEntity(pLevel, pos.x, pos.y, pos.z, output, vel.x, vel.y, vel.z);
                    pLevel.addFreshEntity(entity);
                    i.remove(Entity.RemovalReason.DISCARDED);
                }
            }
            time++;
            if (time >= MAX_TIME) {
                level.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
            }
        }
    }
    private static class ClientHandler {
        public static void spawnParticles(Level level, Vec3 position) {
            if (Minecraft.getInstance().level == level) {
                for (int i = 0; i < 5; i++) {
                    Vec3 offset = new Vec3(
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f)
                    );
                    Vec3 speed = new Vec3(
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, 0.05f, 0.1f),
                            Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f)
                    );
                    Minecraft.getInstance().particleEngine.createParticle(ParticleRegistry.SMALL_SHATTER.get(), position.x + offset.x, position.y + offset.y, position.z + offset.z, speed.x, speed.y, speed.z);
                }
                Vec3 speed = new Vec3(
                        Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f),
                        Mth.nextFloat(Minecraft.getInstance().level.random, 0.05f, 0.1f),
                        Mth.nextFloat(Minecraft.getInstance().level.random, -0.05f, 0.05f)
                );
                Minecraft.getInstance().particleEngine.createParticle(ParticleRegistry.SHATTER.get(), position.x, position.y, position.z, speed.x, speed.y, speed.z);
            }
        }
    }
}
