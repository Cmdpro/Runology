package com.cmdpro.runology.entity;

import com.cmdpro.runology.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
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
    public Vec3 spawnPos;

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.spawnPos = position();
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
    public float atkTimer;
    public int atk;
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        atkTimer++;
        if (atkTimer >= 100) {
            atk = level().random.nextInt(0, 2);
            if (getHealth() <= getMaxHealth()/2) {
                atk = level().random.nextInt(0, 4);
            }
            atkTimer = 0;
            if (atk == 0) {
                triggerAnim("attackController", "animation.runicoverseer.throw");
                for (int i = 0; i < 8; i++) {
                    VoidBomb voidBomb = new VoidBomb(EntityInit.VOIDBOMB.get(), this, level());
                    voidBomb.setDeltaMovement(calculateViewVector(-20, i * (360 / 8)).multiply(0.75f, 0.75f, 0.75f));
                    voidBomb.minExplodeHeight = (float) spawnPos.y;
                    level().addFreshEntity(voidBomb);
                }
            }
            if (atk == 1) {
                atkTimer = -100;
            }
        }
        if (atk == 1) {
            if (atkTimer <= -20) {

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
        super.addAdditionalSaveData(pCompound);
        pCompound.putDouble("spawnX", spawnPos.x);
        pCompound.putDouble("spawnY", spawnPos.y);
        pCompound.putDouble("spawnZ", spawnPos.z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        spawnPos = new Vec3(pCompound.getDouble("spawnX"), pCompound.getDouble("spawnY"), pCompound.getDouble("spawnZ"));
    }
}
