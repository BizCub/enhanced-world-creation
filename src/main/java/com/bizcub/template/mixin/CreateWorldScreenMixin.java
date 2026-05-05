package com.bizcub.template.mixin;

import com.bizcub.template.Main;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
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
    @Unique private final Map<String, GameRule> gameRules = new HashMap<>();

    @Shadow @Final private WorldCreationUiState uiState;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void screenInit(CallbackInfo ci) {
        uiState.getNormalPresetList().forEach(preset ->
                worldPresets.put(preset.describePreset().getContents().toString().split("'")[1], preset));

        uiState.getGameRules().availableRules().forEach(gameRule -> gameRules.put(gameRule.toString(), gameRule));
        GameRuleMap map = new GameRuleMap.Builder().build();

        uiState.setName("World Creation");
        uiState.setGameMode(Main.GAME_MODES.get("spectator"));
        uiState.setDifficulty(Difficulty.byName("easy"));
        uiState.setAllowCommands(true);

        uiState.setWorldType(worldPresets.get("generator.minecraft.flat"));
        uiState.setSeed("1111111111");
        uiState.setGenerateStructures(false);
        uiState.setBonusChest(true);

        map.set(gameRules.get("locator_bar"), false);
        uiState.setGameRules(new GameRules(uiState.getSettings().dataConfiguration().enabledFeatures(), map));
    }
}
