package com.cmdpro.runology.block.entity;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.block.EnderTransporter;
import com.cmdpro.runology.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.joml.Math;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;

import java.awt.*;

public class EnderTransporterBlockEntity extends BlockEntity implements GeoBlockEntity {
    public EnderTransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ENDERTRANSPORTER.get(), pos, state);
    }
    public enum Mode {
        EXTRACT,
        INSERT
    }
    public enum TransportType {
        ITEM,
        FLUID
    }
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.endertransporter.idle", Animation.LoopType.LOOP));
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
    public DyeColor color = DyeColor.WHITE;
    public Mode mode = Mode.INSERT;
    public TransportType transportType = TransportType.ITEM;
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("color", color.getName());
        pTag.putString("mode", mode.name());
        pTag.putString("transportType", transportType.name());
        pTag.putInt("timer", timer);
    }
    public int timer;
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, EnderTransporterBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pBlockEntity.mode == Mode.EXTRACT) {
                pBlockEntity.timer++;
                if (pBlockEntity.timer >= 20) {
                    pBlockEntity.timer = 0;
                    ChunkPos center = pLevel.getChunkAt(pPos).getPos();
                    ChunkPos[] chunkPositions = {
                            new ChunkPos(center.x + 1, center.z + 1), new ChunkPos(center.x + 1, center.z), new ChunkPos(center.x + 1, center.z - 1),
                            new ChunkPos(center.x, center.z + 1), center, new ChunkPos(center.x, center.z - 1),
                            new ChunkPos(center.x - 1, center.z + 1), new ChunkPos(center.x - 1, center.z), new ChunkPos(center.x - 1, center.z - 1)
                    };
                    double dist = 99999;
                    EnderTransporterBlockEntity toInsert = null;
                    for (ChunkPos i : chunkPositions) {
                        for (BlockEntity o : pLevel.getChunk(i.x, i.z).getBlockEntities().values()) {
                            if (o instanceof EnderTransporterBlockEntity ent) {
                                if (ent.mode == Mode.INSERT && ent.color.equals(pBlockEntity.color) && ent.transportType == pBlockEntity.transportType) {
                                    if (ent.getBlockPos().getCenter().distanceTo(pPos.getCenter()) <= 24 && ent.getBlockPos().getCenter().distanceTo(pPos.getCenter()) <= dist) {
                                        dist = ent.getBlockPos().getCenter().distanceTo(pPos.getCenter());
                                        toInsert = ent;
                                    }
                                }
                            }
                        }
                    }
                    if (toInsert != null) {
                        BlockPos pos = toInsert.getBlockPos().relative(toInsert.getDirection().getOpposite());
                        BlockPos pos2 = pPos.relative(pBlockEntity.getDirection().getOpposite());
                        if (pBlockEntity.transportType == TransportType.FLUID && toInsert.transportType == pBlockEntity.transportType) {
                            BlockEntity ent = pLevel.getBlockEntity(pos);
                            BlockEntity ent2 = pLevel.getBlockEntity(pos2);
                            if (ent != null && ent2 != null) {
                                var cap = ent.getCapability(ForgeCapabilities.FLUID_HANDLER, toInsert.getDirection().getOpposite());
                                var cap2 = ent2.getCapability(ForgeCapabilities.FLUID_HANDLER, pBlockEntity.getDirection().getOpposite());
                                if (cap.isPresent() && cap2.isPresent()) {
                                    IFluidHandler resolved = cap.resolve().get();
                                    IFluidHandler resolved2 = cap2.resolve().get();
                                    boolean movedAnything = false;
                                    for (int i = 0; i < resolved2.getTanks(); i++) {
                                        FluidStack copy = resolved2.getFluidInTank(i).copy();
                                        if (!copy.isEmpty()) {
                                            copy.setAmount(Math.clamp(0, 4000, copy.getAmount()));
                                            int filled = resolved.fill(copy, IFluidHandler.FluidAction.EXECUTE);
                                            resolved2.getFluidInTank(i).shrink(filled);
                                            if (filled > 0) {
                                                movedAnything = true;
                                            }
                                        }
                                    }
                                    if (movedAnything) {
                                        BlockState blockState = ent.getLevel().getBlockState(ent.getBlockPos());
                                        ent.getLevel().sendBlockUpdated(ent.getBlockPos(), blockState, blockState, 3);
                                        ent.setChanged();
                                        BlockState blockState2 = ent2.getLevel().getBlockState(ent2.getBlockPos());
                                        ent2.getLevel().sendBlockUpdated(ent2.getBlockPos(), blockState2, blockState2, 3);
                                        ent2.setChanged();
                                        WorldParticleOptions options = new WorldParticleOptions(LodestoneParticleRegistry.WISP_PARTICLE.get());
                                        options.colorData = ColorParticleData.create(new Color(pBlockEntity.color.getTextColor()), new Color(pBlockEntity.color.getTextColor())).build();
                                        options.scaleData = GenericParticleData.create(0.25f).build();
                                        RunologyUtil.drawLine(options, pPos.getCenter().relative(pBlockEntity.getDirection().getOpposite(), 0.25), toInsert.getBlockPos().getCenter().relative(toInsert.getDirection().getOpposite(), 0.25), pLevel, 0.1f);
                                    }
                                }
                            }
                        }
                        if (pBlockEntity.transportType == TransportType.ITEM && toInsert.transportType == pBlockEntity.transportType) {
                            BlockEntity ent = pLevel.getBlockEntity(pos);
                            BlockEntity ent2 = pLevel.getBlockEntity(pos2);
                            if (ent != null && ent2 != null) {
                                var cap = ent.getCapability(ForgeCapabilities.ITEM_HANDLER, toInsert.getDirection().getOpposite());
                                var cap2 = ent2.getCapability(ForgeCapabilities.ITEM_HANDLER, pBlockEntity.getDirection().getOpposite());
                                if (cap.isPresent() && cap2.isPresent()) {
                                    IItemHandler resolved = cap.resolve().get();
                                    IItemHandler resolved2 = cap2.resolve().get();
                                    boolean movedAnything = false;
                                    for (int i = 0; i < resolved2.getSlots(); i++) {
                                        ItemStack copy = resolved2.getStackInSlot(i).copy();
                                        if (!copy.isEmpty()) {
                                            copy.setCount(Math.clamp(0, 16, copy.getCount()));
                                            ItemStack copy2 = copy.copy();
                                            int o = 0;
                                            while (o < resolved.getSlots()) {
                                                ItemStack copyCopy = copy.copy();
                                                int remove = resolved.insertItem(o, copyCopy, false).getCount();
                                                if (remove < copyCopy.getCount()) {
                                                    movedAnything = true;
                                                }
                                                copy.setCount(remove);
                                                if (remove <= 0) {
                                                    break;
                                                }
                                                o++;
                                            }
                                            if (movedAnything) {
                                                resolved2.extractItem(i, copy2.getCount()-copy.getCount(), false);
                                                break;
                                            }
                                        }
                                    }
                                    if (movedAnything) {
                                        BlockState blockState = ent.getLevel().getBlockState(ent.getBlockPos());
                                        ent.getLevel().sendBlockUpdated(ent.getBlockPos(), blockState, blockState, 3);
                                        ent.setChanged();
                                        BlockState blockState2 = ent2.getLevel().getBlockState(ent2.getBlockPos());
                                        ent2.getLevel().sendBlockUpdated(ent2.getBlockPos(), blockState2, blockState2, 3);
                                        ent2.setChanged();
                                        WorldParticleOptions options = new WorldParticleOptions(LodestoneParticleRegistry.WISP_PARTICLE.get());
                                        options.colorData = ColorParticleData.create(new Color(pBlockEntity.color.getTextColor()), new Color(pBlockEntity.color.getTextColor())).build();
                                        options.scaleData = GenericParticleData.create(0.25f).build();
                                        RunologyUtil.drawLine(options, pPos.getCenter(), toInsert.getBlockPos().getCenter(), pLevel, 0.1f);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public Direction getDirection() {
        if (getBlockState().getValue(EnderTransporter.FACE).equals(AttachFace.CEILING)) {
            return Direction.DOWN;
        }
        if (getBlockState().getValue(EnderTransporter.FACE).equals(AttachFace.WALL)) {
            return getBlockState().getValue(EnderTransporter.FACING);
        }
        return Direction.UP;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        color = DyeColor.byName(tag.getString("color"), DyeColor.WHITE);
        mode = Mode.valueOf(tag.getString("mode"));
        transportType = TransportType.valueOf(tag.getString("transportType"));
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("color", color.getName());
        tag.putString("mode", mode.name());
        tag.putString("transportType", transportType.name());
        return tag;
    }
    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        color = DyeColor.byName(pTag.getString("color"), DyeColor.WHITE);
        mode = Mode.valueOf(pTag.getString("mode"));
        transportType = TransportType.valueOf(pTag.getString("transportType"));
        timer = pTag.getInt("timer");
    }

}
