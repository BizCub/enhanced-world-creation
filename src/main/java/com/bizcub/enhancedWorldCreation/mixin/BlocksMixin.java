package com.bizcub.enhancedWorldCreation.mixin;

import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Inject(method = "register", at = @At("RETURN"))
    private static void getBlocksMap(String string, Block block, CallbackInfoReturnable<Block> cir) {
        Main.BLOCKS.put(string, block);
    }
}
