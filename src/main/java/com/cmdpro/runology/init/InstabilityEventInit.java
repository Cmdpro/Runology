package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.InstabilityEvent;
import com.cmdpro.runology.api.InstabilityEventRunnable;
import com.cmdpro.runology.api.RunicEnergyType;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.RunicScout;
import com.cmdpro.runology.entity.Shatter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.function.Supplier;

public class InstabilityEventInit {
    public static final DeferredRegister<InstabilityEvent> INSTABILITY_EVENTS = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "instabilityevents"), Runology.MOD_ID);

    public static final RegistryObject<InstabilityEvent> RUNICSCOUT = register("runicscout", () -> new InstabilityEvent(new InstabilityEventRunnable() {
        @Override
        public void run(Player player, LevelChunk chunk) {
            Vec3 pos = player.position().add(player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16));
            RunicScout monster = EntityInit.RUNICSCOUT.get().create(player.level());
            monster.setPos(pos);
            while (pos.y < player.level().getMaxBuildHeight() && checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, 1, 0);
                monster.setPos(pos);
            }
            while (pos.y > player.level().getMinBuildHeight() && !checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, -1, 0);
                monster.setPos(pos);
            }
            if (chunk.getLevel().getEntitiesOfClass(RunicScout.class, AABB.ofSize(pos, 80, 80, 80)).size() <= 10) {
                player.level().addFreshEntity(monster);
            } else {
                monster.discard();
            }
        }
    }, 250f));
    public static final RegistryObject<InstabilityEvent> RUNICCONSTRUCT = register("runicconstruct", () -> new InstabilityEvent(new InstabilityEventRunnable() {
        @Override
        public void run(Player player, LevelChunk chunk) {
            Vec3 pos = player.position().add(player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16));
            RunicConstruct monster = EntityInit.RUNICCONSTRUCT.get().create(player.level());
            monster.setPos(pos);
            while (pos.y < player.level().getMaxBuildHeight() && checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, 1, 0);
                monster.setPos(pos);
            }
            while (pos.y > player.level().getMinBuildHeight() && !checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, -1, 0);
                monster.setPos(pos);
            }
            if (chunk.getLevel().getEntitiesOfClass(RunicConstruct.class, AABB.ofSize(pos, 80, 80, 80)).size() <= 10) {
                player.level().addFreshEntity(monster);
            } else {
                monster.discard();
            }
        }
    }, 500f));
    public static final RegistryObject<InstabilityEvent> SHATTER = register("shatter", () -> new InstabilityEvent(new InstabilityEventRunnable() {
        @Override
        public void run(Player player, LevelChunk chunk) {
            Vec3 pos = player.position().add(player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16), player.getRandom().nextInt(-16, 16));
            Shatter monster = EntityInit.SHATTER.get().create(player.level());
            monster.setPos(pos);
            while (pos.y < player.level().getMaxBuildHeight() && checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, 1, 0);
                monster.setPos(pos);
            }
            while (pos.y > player.level().getMinBuildHeight() && !checkSpawnObstruction(player.level(), BlockPos.containing(pos), monster)) {
                pos = pos.add(0, -1, 0);
                monster.setPos(pos);
            }
            if (chunk.getLevel().getEntitiesOfClass(Shatter.class, AABB.ofSize(pos, 80, 80, 80)).size() <= 0) {
                player.level().addFreshEntity(monster);
            } else {
                monster.discard();
            }
        }
    }, 750f));
    private static <T extends InstabilityEvent> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return INSTABILITY_EVENTS.register(name, item);
    }
    public static boolean checkSpawnObstruction(LevelReader pLevel, BlockPos pos, Entity entity) {
        BlockPos blockpos = pos;
        BlockPos blockpos1 = blockpos.below();
        BlockState blockstate = pLevel.getBlockState(blockpos1);
        if (!blockstate.entityCanStandOn(pLevel, blockpos1, entity)) {
            return false;
        } else {
            for(int i = 1; i < 3; ++i) {
                BlockPos blockpos2 = blockpos.above(i);
                BlockState blockstate1 = pLevel.getBlockState(blockpos2);
                if (!NaturalSpawner.isValidEmptySpawnBlock(pLevel, blockpos2, blockstate1, blockstate1.getFluidState(), entity.getType())) {
                    return false;
                }
            }

            return NaturalSpawner.isValidEmptySpawnBlock(pLevel, blockpos, pLevel.getBlockState(blockpos), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && pLevel.isUnobstructed(entity);
        }
    }
}
