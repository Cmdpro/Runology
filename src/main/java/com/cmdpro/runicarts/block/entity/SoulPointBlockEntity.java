package com.cmdpro.runicarts.block.entity;

import com.cmdpro.runicarts.api.ISoulContainer;
import com.cmdpro.runicarts.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoulPointBlockEntity extends BlockEntity implements GeoBlockEntity, ISoulContainer {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private float souls;
    private List<BlockPos> linked;
    public SoulPointBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SOULPOINT.get(), pos, state);
        linked = new ArrayList<>();
    }
    @Override
    public float getMaxSouls() {
        return 1;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putFloat("souls", souls);
        if (!this.linked.isEmpty()) {
            ListTag listtag = new ListTag();
            for(BlockPos i : this.linked) {
                CompoundTag tag2 = new CompoundTag();
                tag2.putInt("x", i.getX());
                tag2.putInt("y", i.getY());
                tag2.putInt("z", i.getZ());
                listtag.add(tag2);
            }
            tag.put("linked", listtag);
        }
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        souls = nbt.getFloat("souls");
        linked.clear();
        if (nbt.contains("linked")) {
            ((ListTag) nbt.get("linked")).forEach((i) -> {
                CompoundTag i2 = ((CompoundTag)i);
                BlockPos pos = new BlockPos(i2.getInt("x"), i2.getInt("y"), i2.getInt("z"));
                linked.add(pos);
            });
        }
    }
    @Override
    public float getSouls() {
        return souls;
    }
    @Override
    public void setSouls(float amount) {
        souls = amount;
    }

    @Override
    public List<BlockPos> getLinked() {
        return linked;
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SoulPointBlockEntity pBlockEntity) {
        pBlockEntity.soulContainerTick(pLevel, pPos, pState, pBlockEntity);
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.soulpoint.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
