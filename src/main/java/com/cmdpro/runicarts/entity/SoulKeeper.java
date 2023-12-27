package com.cmdpro.runicarts.entity;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.init.ItemInit;
import com.cmdpro.runicarts.init.ModCriteriaTriggers;
import com.cmdpro.runicarts.init.ParticleInit;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Vector3f;
import org.lwjgl.system.MathUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SoulKeeper extends Monster implements GeoEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS));
    public SoulKeeper(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }
    @Override
    public void setCustomName(@Nullable Component p_31476_) {
        super.setCustomName(p_31476_);
        this.bossEvent.setName(this.getDisplayName());
    }
    public int spawnAnimTimer = 0;
    public Vec3 ritualPos;
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("spawnanimtimer", spawnAnimTimer);
        pCompound.putInt("revivalphasetimer", revivalPhaseTimer);
        pCompound.putDouble("ritualPosX", ritualPos.x);
        pCompound.putDouble("ritualPosY", ritualPos.y);
        pCompound.putDouble("ritualPosZ", ritualPos.z);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        spawnAnimTimer = tag.getInt("spawnanimtimer");
        revivalPhaseTimer = tag.getInt("revivalphasetimer");
        ritualPos = new Vec3(tag.getDouble("ritualPosX"), tag.getDouble("ritualPosY"), tag.getDouble("ritualPosZ"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }
    @Override
    public void startSeenByPlayer(ServerPlayer p_31483_) {
        super.startSeenByPlayer(p_31483_);
        this.bossEvent.addPlayer(p_31483_);
    }
    @Override
    public void stopSeenByPlayer(ServerPlayer p_31488_) {
        super.stopSeenByPlayer(p_31488_);
        this.bossEvent.removePlayer(p_31488_);
    }

    @Override
    public void swing(InteractionHand pHand, boolean pUpdateSelf) {}

    public int atkTimer;
    public int atk;
    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (spawnAnimTimer < 200) {
            atkTimer += 1;
            if (atkTimer >= 100) {
                atkTimer = 0;
                atk = random.nextInt(0, 2);
                if (revivalPhaseTimer > 200) {
                    atk = random.nextInt(0, 4);
                }
                if (atk == 2) {
                    boolean succeed = false;
                    for (LivingEntity i : level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, AABB.ofSize(ritualPos, 20f, 20f, 20f))) {
                        if (i instanceof Player) {
                            continue;
                        }
                        if (i.getMaxHealth() > 30) {
                            continue;
                        }
                        if (ritualPos.distanceTo(i.position()) > 10) {
                            continue;
                        }
                        setPos(i.position());
                        i.setLastHurtByMob(this);
                        i.kill();
                        heal(i.getHealth());
                        succeed = true;
                        break;
                    }
                    if (succeed == false) {
                        atk = 0;
                    }
                }
                if (atk == 3) {
                    atkTimer = -20;
                    boolean succeed = false;
                    for (LivingEntity i : level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, AABB.ofSize(ritualPos, 20f, 20f, 20f))) {
                        if (i instanceof Player) {
                            continue;
                        }
                        if (i.getMaxHealth() > 30) {
                            continue;
                        }
                        if (ritualPos.distanceTo(i.position()) > 10) {
                            continue;
                        }
                        ((ServerLevel)level()).sendParticles(ParticleInit.SOUL.get(), i.position().x, i.position().y+1f, i.position().z, 50, 0, 0, 0, 0.75f);
                        succeed = true;
                    }
                    if (succeed == false) {
                        atk = 0;
                    }
                }
                if (atk == 0) {
                    for (int i = 0; i < 2; i++) {
                        Zombie zombie = new Zombie(EntityType.ZOMBIE, level());
                        zombie.setPos(position());
                        zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
                        level().addFreshEntity(zombie);
                    }
                    for (int i = 0; i < 2; i++) {
                        Skeleton skeleton = new Skeleton(EntityType.SKELETON, level());
                        skeleton.setPos(position());
                        skeleton.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW,1));
                        skeleton.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
                        level().addFreshEntity(skeleton);
                    }
                }
                if (atk == 1) {
                    atkTimer = -20;
                    ArrayList<Player> players = new ArrayList<>();
                    if (ritualPos != null) {
                        for (Player i : level().players()) {
                            if (ritualPos.distanceTo(i.position()) <= 10) {
                                players.add(i);
                            }
                        }
                    } else {
                        for (Player i : level().players()) {
                            if (position().distanceTo(i.position()) <= 10) {
                                players.add(i);
                            }
                        }
                    }
                    setPos(players.get(RandomUtils.nextInt(0, players.size())).position());
                }
            }
            if (atk == 1 && atkTimer == 0) {
                ((ServerLevel)level()).sendParticles(ParticleInit.SOUL.get(), position().x, position().y+1f, position().z, 200, 0, 0, 0, 0.75f);
                level().playSound(null, position().x, position().y, position().z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0f, 1.0f);
                List<LivingEntity> entities = level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, AABB.ofSize(position(), 5f, 5f, 5f));
                for (LivingEntity p : entities) {
                    p.hurt(level().damageSources().mobAttack(this), 20);
                }
            }
            if (atk == 3 && atkTimer == 0) {
                for (LivingEntity i : level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, AABB.ofSize(ritualPos, 20f, 20f, 20f))) {
                    if (i instanceof Player) {
                        continue;
                    }
                    if (i.getMaxHealth() > 30) {
                        continue;
                    }
                    if (ritualPos.distanceTo(i.position()) > 10) {
                        continue;
                    }
                    i.setLastHurtByMob(this);
                    i.kill();
                    List<LivingEntity> entities = level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, AABB.ofSize(position(), 5f, 5f, 5f));
                    for (LivingEntity p : entities) {
                        p.hurt(level().damageSources().mobAttack(this), 20);
                    }
                    ((ServerLevel)level()).sendParticles(ParticleInit.SOUL.get(), i.position().x, i.position().y+1f, i.position().z, 200, 0, 0, 0, 0.75f);
                    level().playSound(null, i.position().x, i.position().y, i.position().z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0f, 1.0f);
                }
            }
        }
    }

    public static final EntityDataAccessor<Boolean> IS_SPAWN_ANIM = SynchedEntityData.defineId(SoulKeeper.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_PHASE2 = SynchedEntityData.defineId(SoulKeeper.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Vector3f> RITUAL_POS = SynchedEntityData.defineId(SoulKeeper.class, EntityDataSerializers.VECTOR3);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SPAWN_ANIM, true);
        this.entityData.define(IS_PHASE2, false);
        this.entityData.define(RITUAL_POS, new Vector3f(0, 0, 0));
    }
    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            ritualPos = new Vec3(entityData.get(RITUAL_POS).x, entityData.get(RITUAL_POS).y, entityData.get(RITUAL_POS).z);
            if (entityData.get(IS_SPAWN_ANIM)) {
                for (int i = 0; i < 5; i++) {
                    Vec3 offset = new Vec3(RandomUtils.nextDouble(0, 4) - 2d, RandomUtils.nextDouble(0, 4) - 2d, RandomUtils.nextDouble(0, 4) - 2d);
                    Vec3 pos = position().add(0, 1, 0).add(offset);
                    level().addParticle(ParticleInit.SOUL.get(), pos.x, pos.y, pos.z, -offset.x / 4f, -offset.y / 4f, -offset.z / 4f);
                }
            }
            for (int i = 0; i < 360; i++) {
                Vec3 pos = new Vec3(ritualPos.x, ritualPos.y, ritualPos.z);
                pos = pos.add(Math.cos(i)*10, -0.5, Math.sin(i)*10);
                level().addParticle(ParticleInit.SOUL.get(), pos.x+(RandomUtils.nextFloat(0f, 0.2f)-0.1f), pos.y-0.1, pos.z+(RandomUtils.nextFloat(0f, 0.2f)-0.1f), RandomUtils.nextFloat(0f, 0.4f)-0.2f, 0.5f, RandomUtils.nextFloat(0f, 0.4f)-0.2f);
            }
        } else {
            entityData.set(RITUAL_POS, new Vector3f((float)ritualPos.x, (float)ritualPos.y, (float)ritualPos.z));
            if (ritualPos != null) {
                boolean anyPlayers = false;
                for (Player i : level().players()) {
                    if (ritualPos.distanceTo(i.position()) <= 10) {
                        anyPlayers = true;
                    }
                }
                if (!anyPlayers) {
                    remove(RemovalReason.DISCARDED);
                }
                Multiblock ritual = MultiblockDataManager.get().getMultiblock(new ResourceLocation(RunicArts.MOD_ID, "soulritualnoflames"));
                if (
                        !ritual.validate(level(), BlockPos.containing(ritualPos), Rotation.NONE) &&
                                !ritual.validate(level(), BlockPos.containing(ritualPos), Rotation.CLOCKWISE_90) &&
                                !ritual.validate(level(), BlockPos.containing(ritualPos), Rotation.CLOCKWISE_180) &&
                                !ritual.validate(level(), BlockPos.containing(ritualPos), Rotation.COUNTERCLOCKWISE_90)) {
                    remove(RemovalReason.DISCARDED);
                }
            }
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            if (spawnAnimTimer <= 0) {
                if (revivalPhaseTimer <= -1) {
                    setNoAi(false);
                    entityData.set(IS_SPAWN_ANIM, false);
                    entityData.set(IS_PHASE2, false);
                } else if (revivalPhaseTimer <= 200) {
                    revivalPhaseTimer++;
                    setNoAi(true);
                    entityData.set(IS_SPAWN_ANIM, true);
                    entityData.set(IS_PHASE2, true);
                    setHealth(((revivalPhaseTimer)*(getMaxHealth()/200f))+1);
                } else {
                    setNoAi(false);
                    entityData.set(IS_SPAWN_ANIM, false);
                    entityData.set(IS_PHASE2, true);
                }
            } else {
                setNoAi(true);
                entityData.set(IS_SPAWN_ANIM, true);
                setHealth(((200-spawnAnimTimer)*(getMaxHealth()/200f))+1);
                //List<Player> nearbyPlayers = level().getNearbyPlayers(TargetingConditions.forNonCombat(), this, new AABB(position().subtract(30, 30, 30), position().add(30, 30, 30)));
                //if (spawnAnimTimer == 200) {
                //    for (Player i : nearbyPlayers) {
                //        i.sendSystemMessage(Component.translatable("entity.runicarts.soulkeeper.phase1.text1"));
                //    }
                //}
                spawnAnimTimer--;
            }
        }
    }
    public int revivalPhaseTimer = -1;
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (spawnAnimTimer > 0) {
            return false;
        }
        if (revivalPhaseTimer >= 0 && revivalPhaseTimer <= 200) {
            return false;
        }
        if (getHealth()-pAmount <= 0 && revivalPhaseTimer <= -1) {
            revivalPhaseTimer = 0;
            return super.hurt(pSource, getHealth()-1);
        }
        return super.hurt(pSource, pAmount);
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        if (entityData.get(IS_SPAWN_ANIM)) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.soulkeeper.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.soulkeeper.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(RawAnimation.begin().then("animation.soulkeeper.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        ritualPos = position().subtract(0, 5, 0);
        if (!level().isClientSide) {
            for (Player i : level().players()) {
                if (ritualPos.distanceTo(i.position()) <= 10) {
                    ModCriteriaTriggers.SPAWNSOULKEEPER.trigger((ServerPlayer) i);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {

    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PLAYER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }
    protected float getSoundVolume() {
        return 0.2F;
    }

    public void spawn() {
        bossEvent.setProgress(0);
        setHealth(1);
        spawnAnimTimer = 200;
    }
}
