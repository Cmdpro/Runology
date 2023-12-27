package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.api.IAmplifierSoulcastersCrystal;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FullSoulCrystal extends Item {

    public FullSoulCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.getOrCreateTag().contains("entitytype")) {
            String entity = "entity.";
            ResourceLocation entity2;
            entity2 = ResourceLocation.of(pStack.getOrCreateTag().getString("entitytype"), ':');
            entity = entity + entity2.getNamespace() + "." + entity2.getPath();
            pTooltipComponents.add(Component.translatable("item.runicarts.fullsoulcrystal.tooltip", Component.translatable(entity).getString()).withStyle(ChatFormatting.GRAY));
        }
    }
    public CompoundTag getTagFromEntity(Entity entity, CompoundTag tag) {
        tag.putString("entitytype", entity.getEncodeId());
        tag.put("entitynbt", entity.serializeNBT());
        return tag;
    }
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);

            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof SpawnerBlockEntity) {
                    SpawnerBlockEntity spawnerblockentity = (SpawnerBlockEntity)blockentity;
                    EntityType<?> entitytype1 = EntityType.byString(context.getItemInHand().getOrCreateTag().getString("entitytype")).get();
                    spawnerblockentity.setEntityId(entitytype1, level.getRandom());
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }
            ResourceLocation entity2 = new ResourceLocation("", "");
            if (context.getItemInHand().getOrCreateTag().contains("entitytype")) {
                entity2 = ResourceLocation.of(context.getItemInHand().getOrCreateTag().getString("entitytype"), ':');
            }
            CompoundTag nbt = new CompoundTag();
            if (context.getItemInHand().getOrCreateTag().contains("entitynbt")) {
                nbt = (CompoundTag) context.getItemInHand().getOrCreateTag().get("entitynbt");
            }
            EntityType<?> entitytype = ForgeRegistries.ENTITY_TYPES.getValue(entity2);
            Entity spawn = entitytype.spawn((ServerLevel)level, itemstack, context.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (spawn != null) {
                Vec3 pos = spawn.position();
                spawn.deserializeNBT(nbt);
                spawn.setPos(pos);
                itemstack.shrink(1);
                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);

            }

            return InteractionResult.CONSUME;
        }
    }
}
