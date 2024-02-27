package com.cmdpro.runology.mixins;

import com.cmdpro.runology.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void getBrightness(DimensionType pDimensionType, int pLightLevel, CallbackInfoReturnable<Float> ci) {
        if (Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD).is(ItemInit.ECHOGOGGLES.get())) {
            ci.setReturnValue(1.0f);
        }
    }
}
