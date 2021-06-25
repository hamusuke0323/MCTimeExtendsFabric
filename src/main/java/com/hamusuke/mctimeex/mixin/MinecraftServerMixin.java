package com.hamusuke.mctimeex.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyArg(method = "setToDebugWorldProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerWorldProperties;setTimeOfDay(J)V"), index = 0)
    private long setTimeOfDayInSetToDebugWorldProperties(long timeOfDay) {
        return timeOfDay * 72L;
    }
}
