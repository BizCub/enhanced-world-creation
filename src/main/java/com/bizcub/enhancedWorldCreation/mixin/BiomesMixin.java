package com.bizcub.enhancedWorldCreation.mixin;

import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biomes.class)
public class BiomesMixin {

    @Inject(method = "register", at = @At("RETURN"))
    private static void getBiomesMap(int i, ResourceKey<Biome> resourceKey, Biome biome, CallbackInfoReturnable<Biome> cir) {
        Main.BIOMES.put(resourceKey.toString().split(":")[2].split("]")[0], i);
    }
}
