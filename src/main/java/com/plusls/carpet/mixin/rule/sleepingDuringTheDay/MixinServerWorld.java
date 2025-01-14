package com.plusls.carpet.mixin.rule.sleepingDuringTheDay;

import com.plusls.carpet.PcaSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World implements StructureWorldAccess {
    protected MixinServerWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    // 根据当前时间设置夜晚和白天
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", ordinal = 0))
    void redirectSetTimeOfDay(ServerWorld world, long timeOfDay) {
        if (this.isDay() && PcaSettings.sleepingDuringTheDay) {
            world.setTimeOfDay(13000);
        } else {
            world.setTimeOfDay(timeOfDay);
        }
    }
}
