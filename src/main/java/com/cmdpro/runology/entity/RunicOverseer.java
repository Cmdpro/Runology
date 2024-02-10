package com.cmdpro.runology.entity;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


public class RunicOverseer extends Monster implements GeoEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public RunicOverseer(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 5f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }
    public static final EntityDataAccessor<Boolean> INTRO = SynchedEntityData.defineId(RunicOverseer.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(INTRO, true);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public Vec3 spawnPos;

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.spawnPos = position().add(0, -1, 0);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.runicoverseer.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(RawAnimation.begin().then("animation.runicoverseer.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private <E extends GeoAnimatable> PlayState attackPredicate(AnimationState event) {
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
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        Shatter shatter = new Shatter(EntityInit.SHATTER.get(), level());
        shatter.setPos(spawnPos.add(0, 1, 0));
        shatter.oneTimePlayerOnly = true;
        shatter.getEntityData().set(Shatter.OPENED, true);
        level().addFreshEntity(shatter);
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || !introDone;
    }
    public boolean introDone;
    public int introTimer;
    public float atkTimer;
    public int atk;
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        atkTimer++;
        if (atkTimer >= 50) {
            atk = level().random.nextInt(0, 2);
            if (getHealth() <= getMaxHealth()/2) {
                atk = level().random.nextInt(0, 4);
            }
            atkTimer = 0;
            if (atk == 0) {
                triggerAnim("attackController", "animation.runicoverseer.throw");
                for (int i = 0; i < 8; i++) {
                    VoidBomb voidBomb = new VoidBomb(EntityInit.VOIDBOMB.get(), this, level());
                    voidBomb.setDeltaMovement(calculateViewVector(-20, i * (360 / 8)).multiply(0.25f, 0.25f, 0.25f));
                    voidBomb.minExplodeHeight = (float) spawnPos.y;
                    level().addFreshEntity(voidBomb);
                }
            }
            if (atk == 1) {
                atkTimer = -100;
            }
            if (atk == 2) {
                atkTimer = -100;
            }
            if (atk == 3) {
                triggerAnim("attackController", "animation.runicoverseer.beam");
                for (Player i : level().players()) {
                    if (i.position().distanceTo(position()) <= 35) {
                        VoidBeam voidBomb = new VoidBeam(EntityInit.VOIDBEAM.get(), this, level());
                        voidBomb.player = i;
                        Vec3 pos = new Vec3(i.position().x, i.blockPosition().getCenter().y, i.position().z);
                        while (!level().getBlockState(BlockPos.containing(pos)).isSolid() && pos.y > level().getMinBuildHeight()) {
                            pos = pos.add(0, -1, 0);
                        }
                        pos = pos.add(0, 0.5f, 0);
                        voidBomb.setPos(pos);
                        level().addFreshEntity(voidBomb);
                    }
                }
                atkTimer = -100;
            }
        }
        if (atk == 1) {
            if (atkTimer <= -20) {
                if (atkTimer/10 == Math.round(atkTimer/10)) {
                    Player player = null;
                    for (Player i : level().players()) {
                        if (i.position().distanceTo(position()) <= 35) {
                            if (player == null || i.position().distanceTo(position()) <= player.position().distanceTo(position())) {
                                player = i;
                            }
                        }
                    }
                    if (player != null) {
                        Vec3 vec3 = getEyePosition();
                        double d0 = player.getEyePosition().x - vec3.x;
                        double d1 = player.getEyePosition().y - vec3.y;
                        double d2 = player.getEyePosition().z - vec3.z;
                        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                        VoidBullet voidBullet = new VoidBullet(EntityInit.VOIDBULLET.get(), this, level());
                        voidBullet.setDeltaMovement(calculateViewVector(
                                Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)))),
                                Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F)
                        ).multiply(0.75f, 0.75f, 0.75f));
                        level().addFreshEntity(voidBullet);
                    }
                }
            }
        }
        if (atk == 2) {
            if (atkTimer <= 0) {
                if (atkTimer/20 == Math.round(atkTimer/20)) {
                    VoidBomb voidBomb = new VoidBomb(EntityInit.VOIDBOMB.get(), this, level());
                    voidBomb.setDeltaMovement(new Vec3(0, 1, 0));
                    voidBomb.minExplodeHeight = (float) spawnPos.y;
                    level().addFreshEntity(voidBomb);
                    triggerAnim("attackController", "animation.runicoverseer.throw");
                    Vec3 pos = spawnPos;
                    while (pos.distanceTo(spawnPos) <= 3) {
                        pos = new Vec3(spawnPos.x + random.nextInt(-10, 10), position().y, spawnPos.z + random.nextInt(-10, 10));
                    }
                    setPos(pos);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController(this, "controller", 0, this::predicate));
        data.add(new AnimationController(this, "attackController", 0, this::attackPredicate)
                .triggerableAnim("animation.runicoverseer.throw", RawAnimation.begin().then("animation.runicoverseer.throw", Animation.LoopType.PLAY_ONCE))
                .triggerableAnim("animation.runicoverseer.beam", RawAnimation.begin().then("animation.runicoverseer.beam", Animation.LoopType.PLAY_ONCE))
        );
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }


    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        playSound(SoundEvents.IRON_GOLEM_STEP);
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        introDone = true;
        super.addAdditionalSaveData(pCompound);
        pCompound.putDouble("spawnX", spawnPos.x);
        pCompound.putDouble("spawnY", spawnPos.y);
        pCompound.putDouble("spawnZ", spawnPos.z);
        pCompound.putInt("introTimer", introTimer);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!introDone && !level().isClientSide) {
            for (Player i : level().players()) {
                if (i.position().distanceTo(position()) <= 35) {
                    i.sendSystemMessage(Component.translatable("entity.runology.runicoverseer.interrupt"));
                    introTimer = (100*6)+1;
                    introDone = true;
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        spawnPos = new Vec3(pCompound.getDouble("spawnX"), pCompound.getDouble("spawnY"), pCompound.getDouble("spawnZ"));
        introTimer = pCompound.getInt("introTimer");
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }
    @Override
    public void setCustomName(@javax.annotation.Nullable Component p_31476_) {
        super.setCustomName(p_31476_);
        this.bossEvent.setName(this.getDisplayName());
    }
    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            if (position().y < spawnPos.y-1) {
                remove(RemovalReason.DISCARDED);
            }
            Player player = null;
            for (Player i : level().players()) {
                if (i.position().distanceTo(position()) <= 35) {
                    player = i;
                }
            }
            if (player == null) {
                remove(RemovalReason.DISCARDED);
            }
            if (introTimer <= 100*6) {
                String[] intro = {
                        "entity.runology.runicoverseer.dialogue1",
                        "entity.runology.runicoverseer.dialogue2",
                        "entity.runology.runicoverseer.dialogue3",
                        "entity.runology.runicoverseer.dialogue4",
                        "entity.runology.runicoverseer.dialogue5",
                        "entity.runology.runicoverseer.dialogue6",
                        "entity.runology.runicoverseer.dialogue7"
                };
                if (Math.round((float)introTimer/100f) == (float)introTimer/100f) {
                    for (Player i : level().players()) {
                        if (i.position().distanceTo(position()) <= 35) {
                            i.sendSystemMessage(Component.translatable(intro[introTimer/100]));
                        }
                    }
                }
                introTimer++;
                introDone = false;
            } else {
                introDone = true;
            }
            entityData.set(INTRO, !introDone);
        }
    }

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS));
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
}
