package com.cmdpro.runology.block.world;

import com.cmdpro.databank.DatabankUtils;
import com.cmdpro.databank.multiblock.Multiblock;
import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.data.shatterupgrades.ShatterUpgradeManager;
import com.cmdpro.runology.datamaps.RunologyDatamaps;
import com.cmdpro.runology.datamaps.ShatterConversionMap;
import com.cmdpro.runology.entity.ShatterZap;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.ShatterExplodeS2CPacket;
import com.cmdpro.runology.recipe.RecipeUtil;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import com.cmdpro.runology.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public class ShatterBlockEntity extends BlockEntity {
    public ShatterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SHATTER.get(), pos, state);
        instabilityExplardTimer = INSTABILITY_EXPLARD_TIME;
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
        for (var i : ShatterUpgradeManager.types.values()) {
            Multiblock multiblock = MultiblockManager.multiblocks.get(i.multiblock);
            if (multiblock != null) {
                if (multiblock.checkMultiblockAll(level, getBlockPos().below())) {
                    power += i.power;
                }
            }
        }
    }
    private void calculateStability() {
        stability = 5-power;
        for (var i : ShatterUpgradeManager.types.values()) {
            Multiblock multiblock = MultiblockManager.multiblocks.get(i.multiblock);
            if (multiblock != null) {
                if (multiblock.checkMultiblockAll(level, getBlockPos().below())) {
                    stability += i.stability;
                }
            }
        }
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("surgeCooldown", surgeCooldown);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        surgeCooldown = tag.getInt("surgeCooldown");
    }

    @Override
    public void setRemoved() {
        if (level != null && level.isClientSide) {
            ClientHandler.stopSound(this);
        }
        super.setRemoved();
    }

    public int surgeCooldown;
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
            boolean soundPlaying = instabilityExplardTimer < INSTABILITY_EXPLARD_TIME;
            if (soundPlaying && !ClientHandler.isPlayingSound(this)) {
                //TODO: Update time to match progress
                ClientHandler.startSound(this);
            }
            if (!soundPlaying && ClientHandler.isPlayingSound(this)) {
                ClientHandler.stopSound(this);
            }
        } else {
            calculatePower();
            calculateStability();
            for (ItemEntity i : items) {
                if (!i.onGround()) {
                    continue;
                }
                SingleRecipeInput input = new SingleRecipeInput(i.getItem());
                Optional<RecipeHolder<ShatterInfusionRecipe>> recipe = RecipeUtil.getShatterImbuementRecipe(level, 20, input);
                if (recipe.isPresent()) {
                    i.setData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER, i.getData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER)+1);
                    if (i.getData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER) >= 100) {
                        i.removeData(AttachmentTypeRegistry.SHATTER_ITEM_CONVERSION_TIMER);
                        i.getItem().shrink(1);
                        if (i.getItem().isEmpty()) {
                            i.remove(Entity.RemovalReason.KILLED);
                        }
                        ItemStack book = recipe.get().value().assemble(input, level.registryAccess());
                        ItemEntity item = new ItemEntity(pLevel, i.position().x, i.position().y, i.position().z, book);
                        pLevel.addFreshEntity(item);
                    }
                }
            }
            List<Player> players = pLevel.getEntitiesOfClass(Player.class, AABB.ofSize(center, 15, 15, 15));
            for (Player i : players) {
                CriteriaTriggerRegistry.FIND_SHATTER.get().trigger((ServerPlayer)i);
            }
            if (stability <= -3) {
                for (LivingEntity i : level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pPos.getCenter(), 30, 30, 30))) {
                    if (i.position().distanceTo(pPos.getCenter()) <= 15) {
                        if (i.hurt(i.damageSources().source(DamageTypeRegistry.shatterZap), 2)) {
                            ShatterZap attack = new ShatterZap(EntityRegistry.SHATTER_ZAP.get(), pPos.getCenter().add(0, 0.5f, 0), pLevel, i);
                            pLevel.addFreshEntity(attack);
                        }
                    }
                }
            }
            if (stability <= -4) {
                surgeCooldown--;
                if (surgeCooldown <= 0) {
                    surgeCooldown = 90*20;
                    if (stability <= -6) {
                        surgeCooldown = 45*20;
                    }
                    if (level.getBlockEntity(getBlockPos().below()) instanceof ShatteredFocusBlockEntity ent) {
                        ent.path.startSurge(level, stability <= -6 ? 20*20 : 10*20);
                    }
                }
            } else {
                surgeCooldown = 20*20;
            }
            if (stability <= -10) {
                instabilityExplardTimer--;
                updateBlock();
                if (instabilityExplardTimer <= 0) {
                    explard(pLevel, getBlockPos());
                }
            } else {
                if (instabilityExplardTimer < INSTABILITY_EXPLARD_TIME) {
                    instabilityExplardTimer = INSTABILITY_EXPLARD_TIME;
                    updateBlock();
                }
            }
        }
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (!pkt.getTag().isEmpty()) {
            CompoundTag tag = pkt.getTag();
            instabilityExplardTimer = tag.getInt("instabilityExplardTimer");
        }
    }
    public void decodeUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {

    }
    public boolean isPowered;
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("instabilityExplardTimer", instabilityExplardTimer);
        return tag;
    }
    public int instabilityExplardTimer;
    public static final int INSTABILITY_EXPLARD_TIME = 200;
    public void explard(Level level, BlockPos blockPos) {
        ModMessages.sendToPlayersNear(new ShatterExplodeS2CPacket(blockPos), (ServerLevel)level, blockPos.getCenter(), 32);
        Vec3 center = blockPos.getCenter();
        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
        level.explode(null, blockPos.getCenter().x, blockPos.getCenter().y, blockPos.getCenter().z, 6, false, Level.ExplosionInteraction.TNT);
        HashMap<BlockPos, Block> toPlace = new HashMap<>();
        for (int x = -10; x <= 10; x++) {
            for (int y = -10; y <= 10; y++) {
                for (int z = -10; z <= 10; z++) {
                    BlockPos pos = blockPos.offset(x, y, z);
                    Vec3 vec = pos.getCenter();
                    if (vec.distanceTo(center) <= 10) {
                        BlockState state = level.getBlockState(pos);
                        ShatterConversionMap data = state.getBlockHolder().getData(RunologyDatamaps.SHATTER_CONVERSION);
                        if (data != null) {
                            List<Block> blocks = data.convertTo();
                            Block block = blocks.get(Mth.randomBetweenInclusive(level.random, 0, blocks.size()-1));
                            BlockState newState = DatabankUtils.changeBlockType(state, block);
                            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
                            data.placeAbove().ifPresent((config) -> {
                                if (!config.blocks().isEmpty()) {
                                    if (config.rarity() <= 0 || level.random.nextIntBetweenInclusive(0, config.rarity()) == 0) {
                                        Block place = config.blocks().get(Mth.randomBetweenInclusive(level.random, 0, config.blocks().size() - 1));
                                        toPlace.put(pos.above(), place);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
        for (Map.Entry<BlockPos, Block> i : toPlace.entrySet()) {
            if (level.getBlockState(i.getKey()).canBeReplaced()) {
                BlockState state = i.getValue().defaultBlockState();
                level.setBlock(i.getKey(), state, Block.UPDATE_CLIENTS);
                if (i.getValue() instanceof DoublePlantBlock) {
                    DoublePlantBlock.placeAt(level, state, i.getKey(), Block.UPDATE_CLIENTS);
                }
            }
        }
    }
    public SoundInstance getUnstableSound() {
        if (unstableSound == null) {
            Vec3 pos = getBlockPos().getCenter();
            unstableSound = new SimpleSoundInstance(
                    SoundRegistry.SHATTER_UNSTABLE.value(), SoundSource.BLOCKS, 1, 1, RandomSource.create(0), pos.x, pos.y, pos.z
            );
        }
        return unstableSound;
    }
    public SoundInstance unstableSound;
    private static class ClientHandler {
        public static void startSound(ShatterBlockEntity entity) {
            Minecraft.getInstance().getSoundManager().play(entity.getUnstableSound());
        }
        public static void stopSound(ShatterBlockEntity entity) {
            Minecraft.getInstance().getSoundManager().stop(entity.getUnstableSound());
        }
        public static boolean isPlayingSound(ShatterBlockEntity entity) {
            return Minecraft.getInstance().getSoundManager().isActive(entity.getUnstableSound());
        }
    }
}
