package io.github.bizcub.enhancedWorldCreation;

import io.github.bizcub.enhancedWorldCreation.config.Config;
import io.github.bizcub.enhancedWorldCreation.config.SimpleConfig;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MOD_ID = "enhanced_world_creation";
    public static final Map<String, WorldCreationUiState.SelectedGameMode> GAME_MODES = new HashMap<>();

    public static String iconPath = "";
    public static String resourcePackPath = "";

    public static void init(boolean hasSimpleConfig) {
        if (hasSimpleConfig) {
            Config.set(SimpleConfig.getInstance().get());
        }

        GAME_MODES.put("survival", WorldCreationUiState.SelectedGameMode.SURVIVAL);
        GAME_MODES.put("hardcore", WorldCreationUiState.SelectedGameMode.HARDCORE);
        GAME_MODES.put("creative", WorldCreationUiState.SelectedGameMode.CREATIVE);
        GAME_MODES.put("spectator", WorldCreationUiState.SelectedGameMode.DEBUG);
    }
}
