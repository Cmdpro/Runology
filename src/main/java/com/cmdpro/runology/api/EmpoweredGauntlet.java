package com.cmdpro.runology.api;

import com.cmdpro.runology.entity.Shatter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EmpoweredGauntlet extends Item {
    public Item returnsTo;
    public EmpoweredGauntlet(Properties properties, Item returnsTo) {
        super(properties);
        this.returnsTo = returnsTo;
    }
}
