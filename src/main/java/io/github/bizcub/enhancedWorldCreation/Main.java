package io.github.bizcub.enhancedWorldCreation;

import io.github.bizcub.enhancedWorldCreation.config.ClothConfig;
import io.github.bizcub.enhancedWorldCreation.config.Config;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import io.github.bizcub.enhancedWorldCreation.config.SimpleConfig;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MOD_ID = /*$ mod_id*/ "enhanced_world_creation";
    public static final Map<String, WorldCreationUiState.SelectedGameMode> GAME_MODES = new HashMap<>();
    public static final Map<String, ResourceKey<Biome>> BIOMES = new HashMap<>();
    public static final Map<String, Block> BLOCKS = new HashMap<>();

    public static String iconPath = "";
    public static String resourcePackPath = "";

    public static void init() {
        if (ConfigHelper.isSimpleConfigLoaded()) {
            Config.set(SimpleConfig.getInstance().get());
        } else if (ConfigHelper.isClothConfigLoaded()) {
            Config.set(ClothConfig.getInstance());
        }

        GAME_MODES.put("survival", WorldCreationUiState.SelectedGameMode.SURVIVAL);
        GAME_MODES.put("hardcore", WorldCreationUiState.SelectedGameMode.HARDCORE);
        GAME_MODES.put("creative", WorldCreationUiState.SelectedGameMode.CREATIVE);
        GAME_MODES.put("spectator", WorldCreationUiState.SelectedGameMode.DEBUG);
    }
}
