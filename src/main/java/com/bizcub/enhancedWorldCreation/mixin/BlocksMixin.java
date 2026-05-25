package com.bizcub.enhancedWorldCreation.mixin;

import org.spongepowered.asm.mixin.Mixin;

//? <1.19.4 {
/*import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

*///?} else {
@Mixin(value = {})
class BlocksMixin {

}//?}
