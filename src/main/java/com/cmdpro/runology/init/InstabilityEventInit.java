package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.InstabilityEvent;
import com.cmdpro.runology.api.InstabilityEventRunnable;
import com.cmdpro.runology.api.RunicEnergyType;
import com.cmdpro.runology.entity.RunicConstruct;
import com.cmdpro.runology.entity.RunicScout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
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
            Vec3 pos = player.position().add(player.level().random.nextInt(-16, 16), player.level().random.nextInt(-16, 16), player.level().random.nextInt(-16, 16));
            while (pos.y < player.level().getMaxBuildHeight() && !NaturalSpawner.isValidEmptySpawnBlock(player.level(), new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z)).above(), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))).getFluidState(), EntityInit.RUNICSCOUT.get())) {
                pos = pos.add(0, 1, 0);
            }
            while (pos.y > player.level().getMinBuildHeight() && NaturalSpawner.isValidEmptySpawnBlock(player.level(), new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z)).above(), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))).getFluidState(), EntityInit.RUNICSCOUT.get())) {
                pos = pos.add(0, -1, 0);
            }
            pos = pos.add(0, 2, 0);
            RunicScout monster = EntityInit.RUNICSCOUT.get().create(player.level());
            monster.setPos(pos);
            player.level().addFreshEntity(monster);
        }
    }, 250f));
    public static final RegistryObject<InstabilityEvent> RUNICCONSTRUCT = register("runicconstruct", () -> new InstabilityEvent(new InstabilityEventRunnable() {
        @Override
        public void run(Player player, LevelChunk chunk) {
            Vec3 pos = player.position().add(player.level().random.nextInt(-16, 16), player.level().random.nextInt(-16, 16), player.level().random.nextInt(-16, 16));
            while (pos.y < player.level().getMaxBuildHeight() && !NaturalSpawner.isValidEmptySpawnBlock(player.level(), new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z)).above(), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))).getFluidState(), EntityInit.RUNICCONSTRUCT.get())) {
                pos = pos.add(0, 1, 0);
            }
            while (pos.y > player.level().getMinBuildHeight() && NaturalSpawner.isValidEmptySpawnBlock(player.level(), new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z)).above(), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))), chunk.getBlockState(new BlockPos(new Vec3i((int)pos.x, (int)pos.y, (int)pos.z))).getFluidState(), EntityInit.RUNICCONSTRUCT.get())) {
                pos = pos.add(0, -1, 0);
            }
            pos = pos.add(0, 2, 0);
            RunicConstruct monster = EntityInit.RUNICCONSTRUCT.get().create(player.level());
            monster.setPos(pos);
            player.level().addFreshEntity(monster);
        }
    }, 500f));
    private static <T extends InstabilityEvent> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return INSTABILITY_EVENTS.register(name, item);
    }
}
