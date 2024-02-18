package com.cmdpro.runology.entity;

import com.cmdpro.runology.ShatterRealmTeleporter;
import com.cmdpro.runology.api.EmpoweredGauntlet;
import com.cmdpro.runology.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.jarjar.selection.util.Constants;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import javax.annotation.Nullable;
import java.util.UUID;

public class Shatter extends Entity implements GeoEntity {
    public Shatter(EntityType<Shatter> entityType, Level world) {
        super(entityType, world);
        blocksBuilding = true;
        this.oneTimePlayerOnly = false;
        this.lifeTime = -1;
        this.hasLifeTime = false;
    }
    public Shatter(EntityType<Shatter> entityType, Level world, boolean oneTimePlayerOnly) {
        this(entityType, world);
        this.oneTimePlayerOnly = oneTimePlayerOnly;
    }
    public Shatter(EntityType<Shatter> entityType, Level world, boolean oneTimePlayerOnly, int lifeTime) {
        this(entityType, world);
        this.oneTimePlayerOnly = oneTimePlayerOnly;
        this.lifeTime = lifeTime;
        this.hasLifeTime = true;
    }
    public boolean oneTimePlayerOnly;

    @Override
    public boolean isPickable() {
        return true;
    }

    public static final EntityDataAccessor<Boolean> OPENED = SynchedEntityData.defineId(Shatter.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EXHAUSTED = SynchedEntityData.defineId(Shatter.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> ROTX = SynchedEntityData.defineId(Shatter.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROTY = SynchedEntityData.defineId(Shatter.class, EntityDataSerializers.FLOAT);
    @Override
    protected void defineSynchedData() {
        this.entityData.define(OPENED, false);
        this.entityData.define(EXHAUSTED, false);
        this.entityData.define(ROTX, 0f);
        this.entityData.define(ROTY, (float)random.nextInt(0, 360));
    }
    public int timer;
    public int lifeTime;
    public boolean hasLifeTime;
    public int exhaustion;

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (!pPlayer.level().isClientSide) {
            if (pPlayer.getItemInHand(pHand).getItem() instanceof EmpoweredGauntlet gauntlet && !opened) {
                entityData.set(OPENED, true);
                triggerAnim("attackController", "animation.shatter.open");
                ItemStack stack = pPlayer.getItemInHand(pHand);
                pPlayer.setItemInHand(pHand, new ItemStack(gauntlet.returnsTo, stack.getCount(), stack.getTag()));
                return InteractionResult.SUCCESS;
            }
        }
        return super.interact(pPlayer, pHand);
    }



    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            opened = entityData.get(OPENED);
            if (opened) {
                entityData.set(EXHAUSTED, false);
                for (LivingEntity i : level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                    if (i instanceof Player || !oneTimePlayerOnly) {
                        if (i.canChangeDimensions()) {
                            if (i.isOnPortalCooldown()) {
                                i.setPortalCooldown();
                            } else {
                                MinecraftServer minecraftserver = level().getServer();
                                ResourceKey<Level> resourcekey = i.level().dimension() == DimensionInit.SHATTERREALM ? Level.OVERWORLD : DimensionInit.SHATTERREALM;
                                ServerLevel serverlevel1 = minecraftserver.getLevel(resourcekey);
                                if (serverlevel1 != null && !i.isPassenger()) {
                                    i.setPortalCooldown();
                                    i.changeDimension(serverlevel1, new ShatterRealmTeleporter(serverlevel1));
                                    if (oneTimePlayerOnly) {
                                        remove(RemovalReason.DISCARDED);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                exhaustion++;
                if (exhaustion >= 20*(60*5)) {
                    entityData.set(EXHAUSTED, true);
                    for (Entity i : ((ServerLevel)level()).getEntities().getAll()) {
                        if (i != null) {
                            if (i.position().distanceTo(position()) <= 5) {
                                if (i.hurt(damageSources().magic(), 5)) {
                                    ShatterAttack attack = new ShatterAttack(EntityInit.SHATTERATTACK.get(), this, level(), i);
                                    level().addFreshEntity(attack);
                                }
                            }
                        }
                    }
                } else {
                    entityData.set(EXHAUSTED, false);
                }
                if (exhaustion >= 20*(60*8f)) {
                    remove(RemovalReason.DISCARDED);
                }
            }
            if (hasLifeTime) {
                if (lifeTime > 0) {
                    lifeTime--;
                } else if (lifeTime <= 0) {
                    remove(RemovalReason.DISCARDED);
                }
            }
            timer++;
            if (timer >= 25) {
                if (random.nextBoolean()) {
                    int tries = 0;
                    boolean successful = false;
                    while (tries < 5 && !successful) {
                        Vec3 offset = new Vec3(random.nextInt(-8, 8), random.nextInt(-8, 8), random.nextInt(-8, 8));
                        BlockPos pos = BlockPos.containing(position().add(offset));
                        if (level().getBlockState(pos).is(TagInit.Blocks.SHATTERSTONEREPLACEABLE)) {
                            level().setBlock(pos, BlockInit.SHATTERSTONE.get().defaultBlockState(), Block.UPDATE_ALL);
                            successful = true;
                        }
                        if (level().getBlockState(pos).is(TagInit.Blocks.SHATTERWOODREPLACEABLE)) {
                            level().setBlock(pos, BlockInit.PETRIFIEDSHATTERWOOD.get().defaultBlockState(), Block.UPDATE_ALL);
                            successful = true;
                        }
                        if (level().getBlockState(pos).is(TagInit.Blocks.MYSTERIUMOREREPLACEABLE)) {
                            level().setBlock(pos, BlockInit.MYSTERIUMORE.get().defaultBlockState(), Block.UPDATE_ALL);
                            successful = true;
                        }
                        if (level().getBlockState(pos).is(TagInit.Blocks.SHATTERREMOVES)) {
                            level().removeBlock(pos, false);
                            successful = true;
                        }
                        tries++;
                    }
                }
            }
        } else {
            Player player = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
            if (entityData.get(EXHAUSTED)) {
                double dist = player.position().distanceTo(position());
                if (dist <= 7.5f) {
                    ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(1).setIntensity(1f-((float)dist/15f)));
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        entityData.set(OPENED, pCompound.getBoolean("opened"));
        oneTimePlayerOnly = pCompound.getBoolean("oneTimePlayerOnly");
        lifeTime = pCompound.getInt("lifeTime");
        hasLifeTime = pCompound.getBoolean("hasLifeTime");
        exhaustion = pCompound.getInt("exhaustion");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("opened", entityData.get(OPENED));
        pCompound.putBoolean("oneTimePlayerOnly", oneTimePlayerOnly);
        pCompound.putInt("lifeTime", lifeTime);
        pCompound.putBoolean("hasLifeTime", hasLifeTime);
        pCompound.putInt("exhaustion", exhaustion);
    }

    //@Override
    //public boolean isNoGravity() {
    //    return true;
    //}
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    public boolean opened;
    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        event.getController().setAnimation(RawAnimation.begin().then("animation.shatter.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private <E extends GeoAnimatable> PlayState openPredicate(AnimationState event) {
        return PlayState.CONTINUE;
    }
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
        data.add(attackController);
    }
    public AnimationController attackController = new AnimationController(this, "attackController", 0, this::openPredicate)
            .triggerableAnim("animation.shatter.open", RawAnimation.begin().then("animation.shatter.open", Animation.LoopType.PLAY_ONCE));
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
