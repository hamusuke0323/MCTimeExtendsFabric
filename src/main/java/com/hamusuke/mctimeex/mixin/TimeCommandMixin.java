package com.hamusuke.mctimeex.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.server.command.TimeCommand.executeAdd;
import static net.minecraft.server.command.TimeCommand.executeSet;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {
    @Shadow
    private static int executeQuery(ServerCommandSource source, int time) {
        return 0;
    }

    @Shadow
    private static int getDayTime(ServerWorld world) {
        return 0;
    }

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        dispatcher.register((((CommandManager.literal("time").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(((((CommandManager.literal("set").then(CommandManager.literal("day").executes((context) -> {
            return executeSet(context.getSource(), 1000 * 72);
        }))).then(CommandManager.literal("noon").executes((context) -> {
            return executeSet(context.getSource(), 6000 * 72);
        }))).then(CommandManager.literal("night").executes((context) -> {
            return executeSet(context.getSource(), 13000 * 72);
        }))).then(CommandManager.literal("midnight").executes((context) -> {
            return executeSet(context.getSource(), 18000 * 72);
        }))).then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {
            return executeSet(context.getSource(), IntegerArgumentType.getInteger(context, "time"));
        })))).then(CommandManager.literal("add").then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {
            return executeAdd(context.getSource(), IntegerArgumentType.getInteger(context, "time"));
        })))).then(((CommandManager.literal("query").then(CommandManager.literal("daytime").executes((context) -> {
            return executeQuery(context.getSource(), getDayTime(context.getSource().getWorld()));
        }))).then(CommandManager.literal("gametime").executes((context) -> {
            return executeQuery(context.getSource(), (int) (context.getSource().getWorld().getTime() % 2147483647L));
        }))).then(CommandManager.literal("day").executes((context) -> {
            return executeQuery(context.getSource(), (int) (context.getSource().getWorld().getTimeOfDay() / (24000L * 72L) % 2147483647L));
        }))));
        ci.cancel();
    }

    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    private static void getDayTime(ServerWorld world, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) (world.getTimeOfDay() % (24000L * 72L)));
        cir.cancel();
    }
}
