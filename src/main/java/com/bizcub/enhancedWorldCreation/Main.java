package com.bizcub.enhancedWorldCreation;

import com.bizcub.enhancedWorldCreation.config.ModConfig;
//import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MOD_ID = /*$ mod_id*/ "enhanced_world_creation";
    public static final Map<String, CreateWorldScreen.SelectedGameMode> GAME_MODES = new HashMap<>();
    public static final Map<String, Integer> BIOMES = new HashMap<>();

    public static String iconPath;
    public static String resourcePackPath;

    public static void init() {
        getConfig();

        //WorldCreationUiState.SelectedGameMode
        GAME_MODES.put("survival", CreateWorldScreen.SelectedGameMode.SURVIVAL);
        GAME_MODES.put("hardcore", CreateWorldScreen.SelectedGameMode.HARDCORE);
        GAME_MODES.put("creative", CreateWorldScreen.SelectedGameMode.CREATIVE);
        GAME_MODES.put("spectator", CreateWorldScreen.SelectedGameMode.DEBUG);
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
