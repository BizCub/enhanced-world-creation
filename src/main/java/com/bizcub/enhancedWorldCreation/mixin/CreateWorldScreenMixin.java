package com.bizcub.enhancedWorldCreation.mixin;

//? >=1.19.3 {
import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.Utils;
import com.bizcub.enhancedWorldCreation.config.Compat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
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
        if (!Compat.isClothConfigLoaded()) return;

        Map<String, WorldCreationUiState.WorldTypeEntry> worldPresets = new HashMap<>();
        uiState.getAltPresetList().forEach(preset ->
                worldPresets.put(preset.describePreset().getContents().toString().split("'")[1], preset));

        uiState.setName(Main.getConfig().worldName());
        uiState.setGameMode(Main.GAME_MODES.get(Main.getConfig().gameModes().getName()));
        uiState.setDifficulty(Difficulty.byName(Main.getConfig().difficulties().getName()));
        //~ if >=1.21 'setAllowCheats' -> 'setAllowCommands'
        uiState.setAllowCommands(Main.getConfig().allowCommands());

        uiState.setWorldType(worldPresets.get(Main.getConfig().worldTypes().getKey()));
        uiState.setSeed(Main.getConfig().seed());
        uiState.setGenerateStructures(Main.getConfig().generateStructures());
        uiState.setBonusChest(Main.getConfig().bonusChest());

        var registryAccess = uiState.getSettings().worldgenLoadContext();
        if (uiState.getSettings().selectedDimensions().overworld() instanceof FlatLevelSource flatLevelSource) {
            FlatLevelGeneratorSettings settings = flatLevelSource.settings();

            var layersInfo = settings.getLayersInfo();
            layersInfo.clear();
            layersInfo.addAll(Utils.getFlatLayers(registryAccess));

            uiState.updateDimensions(PresetEditor.flatWorldConfigurator(settings.withBiomeAndLayers(
                    layersInfo,
                    settings.structureOverrides(),
                    Utils.getBiomeById(Main.getConfig().flatBiome(), registryAccess)
            )));
        } else {
            uiState.updateDimensions(PresetEditor.fixedBiomeConfigurator(Utils.getBiomeById(
                    Main.getConfig().singleBiome(), registryAccess
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

//?} else {
/*import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.Utils;
import com.bizcub.enhancedWorldCreation.config.Compat;
import com.bizcub.enhancedWorldCreation.gui.ExtraScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldGenSettingsComponent;
import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {

    @Shadow @Mutable @Final public WorldGenSettingsComponent worldGenSettingsComponent;
    @Shadow private String initName;
    @Shadow private boolean commands;
    @Shadow private boolean commandsChanged;
    @Shadow private Difficulty selectedDifficulty;
    @Shadow private Difficulty effectiveDifficulty;
    @Shadow private WorldCreationUiState.SelectedGameMode gameMode;
    @Shadow private String resultFolder;

    @Unique String pathToSavesFolder = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/saves/";
    @Unique private Button button;

    protected CreateWorldScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void screenInit(CallbackInfo ci) {
        if (!Compat.isClothConfigLoaded()) return;

        WorldGenSettingsComponent currentSettings = this.worldGenSettingsComponent;
        RegistryAccess.RegistryHolder registryHolder = currentSettings.registryHolder();

        Map<String, WorldPreset> worldPresets = new HashMap<>();
        WorldPreset.PRESETS.forEach(preset ->
                worldPresets.put(preset.description().toString().split("'")[1], preset));

        this.initName = Main.getConfig().worldName();
        this.gameMode = Main.GAME_MODES.get(Main.getConfig().gameModes().getName());
        this.selectedDifficulty = Difficulty.byName(Main.getConfig().difficulties().getName());
        this.effectiveDifficulty = this.selectedDifficulty;
        if (Main.getConfig().allowCommands()) {
            this.commands = true;
            this.commandsChanged = true;
        }

        if (!Main.getConfig().seed().isEmpty())
            currentSettings.seed = OptionalLong.of(Long.parseLong(Main.getConfig().seed()));
        currentSettings.preset = Optional.of(worldPresets.get(Main.getConfig().worldTypes().getKey()));
        currentSettings.settings = worldPresets.get(Main.getConfig().worldTypes().getKey()).create(
                currentSettings.registryHolder(),
                currentSettings.settings.seed(),
                currentSettings.settings.generateFeatures(),
                currentSettings.settings.generateBonusChest()
        );
        if (!Main.getConfig().generateStructures())
            currentSettings.settings = currentSettings.settings.withFeaturesToggled();
        if (Main.getConfig().bonusChest())
            currentSettings.settings = currentSettings.settings.withBonusChestToggled();

        if (currentSettings.settings.overworld() instanceof FlatLevelSource) {
            FlatLevelGeneratorSettings flatSettings = ((FlatLevelSource) currentSettings.settings.overworld()).settings();
            flatSettings = flatSettings.withLayers(Utils.getFlatLayers(), flatSettings.structureSettings());
            flatSettings.setBiome(() -> Utils.getBiomeById(Main.getConfig().flatBiome(), registryHolder));
            currentSettings.settings =
                    Utils.getSettings(new FlatLevelSource(flatSettings), currentSettings.settings, registryHolder);
        }

        ResourceKey<NoiseGeneratorSettings> noiseGeneratorSettings = NoiseGeneratorSettings.OVERWORLD;
        switch (Main.getConfig().worldTypes()) {
            case SINGLE_BIOME_CAVES:
                noiseGeneratorSettings = NoiseGeneratorSettings.CAVES;
                break;
            case SINGLE_BIOME_FLOATING_ISLANDS:
                noiseGeneratorSettings = NoiseGeneratorSettings.FLOATING_ISLANDS;
                break;
        }
        switch (Main.getConfig().worldTypes()) {
            case SINGLE_BIOME:
            case SINGLE_BIOME_CAVES:
            case SINGLE_BIOME_FLOATING_ISLANDS:
                Registry<NoiseGeneratorSettings> noiseSettingsRegistry = registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
                ResourceKey<NoiseGeneratorSettings> finalNoiseGeneratorSettings = noiseGeneratorSettings;
                currentSettings.settings = Utils.getSettings(
                        new NoiseBasedChunkGenerator(
                                new FixedBiomeSource(Utils.getBiomeById(Main.getConfig().singleBiome(), registryHolder)),
                                currentSettings.settings.seed(),
                                () -> noiseSettingsRegistry.getOrThrow(finalNoiseGeneratorSettings)
                        ),
                        currentSettings.settings,
                        registryHolder
                );
                break;
        }
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void addExtraButton(CallbackInfo info) {
        this.button = this.addButton(Utils.getButton(
                this.width / 2 + 5, 151, 150, 20,
                Utils.getComponent("enhanced_world_creation.extra.button", Utils.ComponentTypes.TRANSLATABLE),
                (button) -> Minecraft.getInstance().setScreen(new ExtraScreen(Minecraft.getInstance().screen))
        ));
    }

    @Inject(method = "setDisplayOptions", at = @At("TAIL"))
    private void screenSetDisplayOptions(boolean bl, CallbackInfo ci) {
        if (this.button != null) this.button.visible = bl;
    }

    @Inject(method = "onCreate", at = @At("HEAD"))
    private void createRPFolder(CallbackInfo ci) {
        new File(pathToSavesFolder + resultFolder).mkdirs();
    }

    @Inject(method = "onCreate", at = @At("TAIL"))
    private void copyResources(CallbackInfo ci) throws IOException {
        Utils.copyResources(pathToSavesFolder, resultFolder);
    }
}*///?}
