package com.hamusuke.mctimeex.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DebugHud.class)
@Environment(EnvType.CLIENT)
public class DebugHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyArgs(method = "getLeftText", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private void format(Args args) {
        if (((String) args.get(1)).contains("Local Difficulty")) {
            Object[] objects = args.get(2);
            args.set(2, new Object[]{objects[0], objects[1], this.client.world.getTimeOfDay() / (24000L * 72L)});
        }
    }
}
