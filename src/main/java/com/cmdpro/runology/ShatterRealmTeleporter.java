package com.cmdpro.runology;

import com.cmdpro.runology.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ShatterRealmTeleporter implements ITeleporter {
    public Level level;
    public ShatterRealmTeleporter(Level level) {
        this.level = level;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        Vec3 pos = entity.position();
        pos = pos.add(0, 200f-pos.y, 0);
        if (destWorld.dimension().equals(Level.OVERWORLD)) {
            pos = new Vec3(pos.x, destWorld.getHeight(Heightmap.Types.WORLD_SURFACE, (int)pos.x, (int)pos.z), pos.z);
        }
        boolean found = false;
        for (int i = -16; i <= 16; i++) {
            for (int o = -16; o <= 16; o++) {
                for (int p = 0; p <= 16; p++) {
                    if (destWorld.getBlockState(BlockPos.containing(pos.x+i, pos.y-p, pos.z+o)).isSolid()) {
                        pos = pos.add(i, -p, o);
                        while (destWorld.getBlockState(BlockPos.containing(pos)).isSolid()) {
                            pos = pos.add(0, 1, 0);
                        }
                        found = true;
                        break;
                    }
                }
                if (found) { break; }
            }
            if (found) { break; }
        }
        if (!found) {
            for (int i = -4; i <= 4; i++) {
                for (int o = -4; o <= 4; o++) {
                    boolean places = true;
                    if (i <= -3 || i >= 3 || o <= -3 || o >= 3) {
                        places = destWorld.random.nextBoolean();
                        if ((i == -4 || i == 4 || o == -4 || o == 4) && places) {
                            places = destWorld.random.nextBoolean();
                        }
                    }
                    if (places) {
                        destWorld.setBlock(BlockPos.containing(pos.add(i, -1, o)), BlockInit.SHATTERSTONE.get().defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
        PortalInfo portalInfo = new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
        return portalInfo;
    }
}
