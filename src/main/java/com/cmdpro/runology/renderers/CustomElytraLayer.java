package com.cmdpro.runology.renderers;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CustomElytraLayer extends ElytraLayer {
    public CustomElytraLayer(RenderLayerParent pRenderer, EntityModelSet pModelSet) {
        super(pRenderer, pModelSet);
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, LivingEntity entity) {
        return stack.is(ItemInit.DRAGONIUMELYTRA.get()) ? new ResourceLocation(Runology.MOD_ID, "textures/entity/dragoniumelytra.png") : stack.is(ItemInit.PRISMATICELYTRA.get()) ? new ResourceLocation(Runology.MOD_ID, "textures/entity/prismaticelytra.png") : new ResourceLocation("textures/entity/elytra.png");
    }

    @Override
    public boolean shouldRender(ItemStack stack, LivingEntity entity) {
        return stack.is(ItemInit.DRAGONIUMELYTRA.get()) || stack.is(ItemInit.PRISMATICELYTRA.get());
    }
}
