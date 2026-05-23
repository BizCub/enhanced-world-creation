package com.bizcub.enhancedWorldCreation;

import com.bizcub.enhancedWorldCreation.config.ModConfig;
//import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MOD_ID = /*$ mod_id*/ "enhanced_world_creation";
    //public static final Map<String, WorldCreationUiState.SelectedGameMode> GAME_MODES = new HashMap<>();

    public static String iconPath;
    public static String resourcePackPath;

    public static void init() {
        getConfig();

//        GAME_MODES.put("survival", WorldCreationUiState.SelectedGameMode.SURVIVAL);
//        GAME_MODES.put("hardcore", WorldCreationUiState.SelectedGameMode.HARDCORE);
//        GAME_MODES.put("creative", WorldCreationUiState.SelectedGameMode.CREATIVE);
//        GAME_MODES.put("spectator", WorldCreationUiState.SelectedGameMode.DEBUG);
    }

    public static ModConfig getConfig() {
        return ModConfig.CONFIG;
    }

    public static ResourceLocation getDefaultId(String id) {
        return
                /*? >=1.21 {*/ /*ResourceLocation.withDefaultNamespace(id);
                *//*?} else*/ new ResourceLocation(id);
    }
}
