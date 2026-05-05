package com.bizcub.template.mixin;

import com.bizcub.template.Main;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Unique private final Map<String, WorldCreationUiState.WorldTypeEntry> worldPresets = new HashMap<>();

    @Shadow @Final private WorldCreationUiState uiState;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void screenInit(CallbackInfo ci) {
        uiState.getNormalPresetList().forEach(preset -> worldPresets.put(preset.describePreset().getString(), preset));

        uiState.setName("World Creation");
        uiState.setGameMode(Main.GAME_MODES.get("spectator"));
        uiState.setDifficulty(Difficulty.byName("easy"));
        uiState.setAllowCommands(true);

        uiState.setWorldType(worldPresets.get("AMPLIFIED"));
        uiState.setSeed("1111111111");
        uiState.setGenerateStructures(false);
        uiState.setBonusChest(true);
    }
}
