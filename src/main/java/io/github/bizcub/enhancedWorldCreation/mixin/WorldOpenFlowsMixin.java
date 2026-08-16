package io.github.bizcub.enhancedWorldCreation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.client.resources.server.DownloadedPackSource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {

    @ModifyVariable(method = "createLevelFromExistingSettings", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private LevelStorageSource.LevelStorageAccess applyBundledResourcePack(LevelStorageSource.LevelStorageAccess levelSourceAccess) {
        DownloadedPackSource packSource = Minecraft.getInstance().getDownloadedPackSource();
        ((WorldOpenFlows) (Object) this).loadBundledResourcePack(packSource, levelSourceAccess);
        return levelSourceAccess;
    }
}
