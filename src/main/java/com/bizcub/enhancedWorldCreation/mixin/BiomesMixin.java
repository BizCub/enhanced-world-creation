package com.bizcub.enhancedWorldCreation.mixin;

import org.spongepowered.asm.mixin.Mixin;

//? <1.19.4 {
/*import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biomes.class)
public class BiomesMixin {

    @Inject(method = "register", at = @At("RETURN"))
    private static void getBiomesMap(/^? <1.18 >>+ ','^/ /^int i,^/ ResourceKey<Biome> resourceKey, Biome biome, /^? >=1.18.2 {^/ CallbackInfo ci /^?} else {^/ /^CallbackInfoReturnable<Biome> cir^//^?}^/) {
        Main.BIOMES.put(resourceKey.toString().split(":")[2].split("]")[0], resourceKey);
    }
}

*///?} else {
@Mixin(value = {})
class BiomesMixin {

}//?}
