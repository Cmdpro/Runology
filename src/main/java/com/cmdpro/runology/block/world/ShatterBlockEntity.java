package com.cmdpro.runology.block.world;

import com.cmdpro.runology.recipe.RecipeUtil;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import com.cmdpro.runology.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class ShatterBlockEntity extends BlockEntity {
    public ShatterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTER.get(), pos, state);
    }
    private float power;
    private float stability;
    public float getPower() {
        return power;
    }
    public float getStability() {
        return stability;
    }
    public int getOutputShatteredFlow() {
        return (int)Math.floor(100f*power);
    }
    private void calculatePower() {
        power = 3;
    }
    private void calculateStability() {
        stability = 3-power;
    }
    @Override
    public void setLevel(Level pLevel) {
        if (level != null) {
            level.getData(AttachmentTypeRegistry.SHATTERS).remove(this);
        }
        super.setLevel(pLevel);
        if (!pLevel.getData(AttachmentTypeRegistry.SHATTERS).contains(this)) {
            pLevel.getData(AttachmentTypeRegistry.SHATTERS).add(this);
        }
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        Vec3 center = getBlockPos().getCenter();
        List<ItemEntity> items = pLevel.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(center, 10, 10, 10));
        if (pLevel.isClientSide) {
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                Optional<RecipeHolder<ShatterInfusionRecipe>> recipe = RecipeUtil.getShatterImbuementRecipe(level, 20, new SingleRecipeInput(i.getItem()));
                if (recipe.isPresent()) {
                    Vec3 diff = i.position().add(0, 0.25, 0).subtract(center).multiply(0.2f, 0.2f, 0.2f);
                    pLevel.addParticle(ParticleRegistry.SHATTER.get(), center.x, center.y, center.z, diff.x, diff.y, diff.z);
                }
            }
        } else {
            calculatePower();
            calculateStability();
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                Optional<RecipeHolder<ShatterInfusionRecipe>> recipe = RecipeUtil.getShatterImbuementRecipe(level, 20, new SingleRecipeInput(i.getItem()));
                if (recipe.isPresent()) {
                    i.setData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER, i.getData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER)+1);
                    if (i.getData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER) >= 100) {
                        i.removeData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER);
                        i.getItem().shrink(1);
                        if (i.getItem().isEmpty()) {
                            i.remove(Entity.RemovalReason.KILLED);
                        }
                        ItemStack book = recipe.get().value().getResultItem(level.registryAccess());
                        ItemEntity item = new ItemEntity(pLevel, i.position().x, i.position().y, i.position().z, book);
                        pLevel.addFreshEntity(item);
                    }
                }
            }
            List<Player> players = pLevel.getEntitiesOfClass(Player.class, AABB.ofSize(center, 15, 15, 15));
            for (Player i : players) {
                CriteriaTriggerRegistry.FIND_SHATTER.get().trigger((ServerPlayer)i);
            }
        }
    }
}
