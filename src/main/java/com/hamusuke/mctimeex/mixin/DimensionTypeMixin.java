package com.hamusuke.mctimeex.mixin;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalLong;

import static net.minecraft.world.dimension.DimensionType.THE_END_ID;
import static net.minecraft.world.dimension.DimensionType.THE_NETHER_ID;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
    @Mutable
    @Shadow
    @Final
    protected static DimensionType THE_NETHER;

    @Mutable
    @Shadow
    @Final
    protected static DimensionType THE_END;

    @Shadow
    public static DimensionType create(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int minimumY, int height, int logicalHeight, BiomeAccessType biomeAccessType, Identifier infiniburn, Identifier skyProperties, float ambientLight) {
        return null;
    }

    @Shadow
    @Final
    private OptionalLong fixedTime;

    static {
        THE_NETHER = create(OptionalLong.of(18000L * 72L), false, true, true, false, 8.0D, false, true, false, true, false, 0, 256, 128, VoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_NETHER.getId(), THE_NETHER_ID, 0.1F);
        THE_END = create(OptionalLong.of(6000L * 72L), false, false, false, false, 1.0D, true, false, false, false, true, 0, 256, 256, VoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_END.getId(), THE_END_ID, 0.0F);
    }

    @Inject(method = "getSkyAngle", at = @At("HEAD"), cancellable = true)
    private void getSkyAngle(long time, CallbackInfoReturnable<Float> cir) {
        double d = MathHelper.fractionalPart(this.fixedTime.orElse(time) / (24000.0D * 72.0D) - 0.25D);
        double e = 0.5D - Math.cos(d * 3.141592653589793D) / 2.0D;
        cir.setReturnValue((float) (d * 2.0D + e) / 3.0F);
        cir.cancel();
    }

    @Inject(method = "getMoonPhase", at = @At("HEAD"), cancellable = true)
    public void getMoonPhase(long time, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) (time / (24000L * 72L) % 8L + 8L) % 8);
        cir.cancel();
    }
}
