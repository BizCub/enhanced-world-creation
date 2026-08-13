package io.github.bizcub.enhancedWorldCreation.mixin;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.enhancedWorldCreation.Utils;
import io.github.bizcub.enhancedWorldCreation.config.Config;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Unique String pathToSavesFolder = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/saves/";

    @Shadow @Final private WorldCreationUiState uiState;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void screenInit(CallbackInfo ci) {
        if (!ConfigHelper.isConfigLoaded()) return;
        var registryAccess = uiState.getSettings().worldgenLoadContext();

        Map<String, WorldCreationUiState.WorldTypeEntry> worldPresets = new HashMap<>();
        uiState.getAltPresetList().forEach(preset ->
                worldPresets.put(preset.describePreset().getContents().toString().split("'")[1], preset));

        //? >=26.2 {
        WorldCreationUiState.WorldTypeEntry flatAll = new WorldCreationUiState.WorldTypeEntry(registryAccess.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT_ALL_DIMENSIONS));
        worldPresets.put(flatAll.describePreset().getContents().toString().split("'")[1], flatAll);//?}

        uiState.setName(Config.get().worldName());
        uiState.setGameMode(Main.GAME_MODES.get(Config.get().gameModes().getName()));
        uiState.setDifficulty(Difficulty.byName(Config.get().difficulties().getName()));
        //~ if >=1.21 'setAllowCheats' -> 'setAllowCommands'
        uiState.setAllowCommands(Config.get().allowCommands());

        uiState.setWorldType(worldPresets.get(Config.get().worldTypes().toString()));
        uiState.setSeed(Config.get().seed());
        uiState.setGenerateStructures(Config.get().generateStructures());
        uiState.setBonusChest(Config.get().bonusChest());

        if (uiState.getSettings().selectedDimensions().overworld() instanceof FlatLevelSource flatLevelSource) {
            FlatLevelGeneratorSettings settings = flatLevelSource.settings();

            var layersInfo = settings.getLayersInfo();
            layersInfo.clear();
            layersInfo.addAll(Utils.getFlatLayers(registryAccess));
            if (Config.get().decoration()) settings.setDecoration();

            uiState.updateDimensions(PresetEditor.flatWorldConfigurator(settings.withBiomeAndLayers(
                    layersInfo,
                    settings.structureOverrides(),
                    Utils.getBiomeById(Config.get().flatBiome(), registryAccess)
            )));
        } else {
            uiState.updateDimensions(PresetEditor.fixedBiomeConfigurator(Utils.getBiomeById(
                    Config.get().singleBiome(), registryAccess
            )));
        }
    }

    @Inject(method = "createNewWorld", at = @At("HEAD"))
    private void createRPFolder(/*? >=1.21.2 {*/ CallbackInfoReturnable<Boolean> cir /*?} else {*/ /*CallbackInfo ci *//*?}*/) {
        new File(pathToSavesFolder + uiState.getTargetFolder() + "/resourcepacks").mkdirs();
    }

    @Inject(method = "createNewWorld", at = @At("TAIL"))
    private void copyResources(/*? >=1.21.2 {*/ CallbackInfoReturnable<Boolean> cir /*?} else {*/ /*CallbackInfo ci *//*?}*/) throws IOException {
        Utils.copyResources(pathToSavesFolder, uiState.getTargetFolder());
    }
}
