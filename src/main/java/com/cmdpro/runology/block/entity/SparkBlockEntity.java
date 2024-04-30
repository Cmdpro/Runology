package com.cmdpro.runology.block.entity;

import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.entity.ShatterAttack;
import com.cmdpro.runology.entity.SparkAttack;
import com.cmdpro.runology.init.BlockEntityInit;
import com.cmdpro.runology.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.RandomUtils;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;

import java.awt.*;

public class SparkBlockEntity extends BlockEntity {
    public SparkBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SPARK.get(), pos, state);
    }
    public int timer = 0;

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("timer", timer);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        timer = pTag.getInt("timer");
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SparkBlockEntity pBlockEntity) {
        if (!pLevel.isClientSide()) {
            if (pBlockEntity.timer >= 20) {
                pBlockEntity.timer = 0;
                for (Entity i : ((ServerLevel)pLevel).getEntities().getAll()) {
                    if (i != null && !(i instanceof Player) && i instanceof LivingEntity) {
                        if (i.position().distanceTo(pPos.getCenter()) <= 2.5) {
                            if (i.hurt(pLevel.damageSources().magic(), 2.5f)) {
                                SparkAttack attack = new SparkAttack(EntityInit.SPARKATTACK.get(), pLevel, pPos.getCenter(), i);
                                pLevel.addFreshEntity(attack);
                                for (LivingEntity o : pLevel.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(i.position(), 2.5, 2.5, 2.5))) {
                                    if (o != i) {
                                        if (o.hurt(pLevel.damageSources().magic(), 1.25f)) {
                                            SparkAttack attack2 = new SparkAttack(EntityInit.SPARKATTACK.get(), pLevel, i.getBoundingBox().getCenter(), o);
                                            pLevel.addFreshEntity(attack2);
                                        }
                                    }
                                }
                                int amount = RandomUtils.nextInt(4, 7);
                                for (int o = 0; o < amount; o++) {
                                    SparkAttack attack2 = new SparkAttack(EntityInit.SPARKATTACK.get(), pLevel, i.getBoundingBox().getCenter(), i.getBoundingBox().getCenter().add(RandomUtils.nextFloat(0, 5)-2.5f, RandomUtils.nextFloat(0, 5)-2.5f, RandomUtils.nextFloat(0, 5)-2.5f));
                                    pLevel.addFreshEntity(attack2);
                                }
                            }
                        }
                    }
                }
            } else {
                pBlockEntity.timer++;
            }
        } else {
            if (pLevel.random.nextInt(0, 2) == 0) {
                WorldParticleOptions options = new WorldParticleOptions(LodestoneParticleRegistry.STAR_PARTICLE.get());
                options.colorData = ColorParticleData.create(Color.YELLOW, Color.YELLOW).build();
                options.scaleData = GenericParticleData.create(0.25f).build();
                pLevel.addParticle(options, pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, 0, 0, 0);
            }
        }
    }
}
