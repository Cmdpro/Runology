package com.cmdpro.runology.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "setup", at = @At(value = "TAIL"), remap = false)
    private void Runology$setup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        CameraAccessor accessor = ((CameraAccessor)this);
        Camera camera = (Camera)(Object)this;

    }
    private void Runology$lookAt(Camera camera, Vec3 target) {
        CameraAccessor accessor = ((CameraAccessor)camera);
        double d0 = target.x - camera.getPosition().x;
        double d1 = target.y - camera.getPosition().y;
        double d2 = target.z - camera.getPosition().z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        accessor.Runology$setRotation(Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F), Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * 180.0F / (float)Math.PI))), 0);
    }
}
