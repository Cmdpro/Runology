package com.cmdpro.runicarts.api;

import com.cmdpro.runicarts.entity.SoulKeeper;
import com.cmdpro.runicarts.init.EntityInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RunicArtsUtil {
    public static Supplier<IForgeRegistry<SoulcasterEffect>> SOULCASTER_EFFECTS_REGISTRY = null;
    public static List<Item> SOULCASTER_CRYSTALS = new ArrayList<>();
    public static SoulKeeper spawnSoulKeeper(Vec3 pos, Level level) {
        SoulKeeper boss = new SoulKeeper(EntityInit.SOULKEEPER.get(), level);
        boss.setPos(pos);
        boss.spawn();
        level.addFreshEntity(boss);
        return boss;
    }
}
