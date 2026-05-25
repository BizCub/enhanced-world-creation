package com.bizcub.enhancedWorldCreation.config;

import com.bizcub.enhancedWorldCreation.Main;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Config(name = Main.MOD_ID)
public class ModClothConfig implements ModConfig, ConfigData {

    public static ModClothConfig getInstance() {
        return AutoConfig.register(ModClothConfig.class, GsonConfigSerializer::new).getConfig();
    }

    public String worldName = ModConfig.super.worldName();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GameModes gameModes = ModConfig.super.gameModes();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Difficulties difficulties = ModConfig.super.difficulties();

    public boolean allowCommands = ModConfig.super.allowCommands();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public WorldTypes worldTypes = ModConfig.super.worldTypes();

    public List<String> flatLayers = ModConfig.super.flatLayers();
    public String flatBiome = ModConfig.super.flatBiome();
    public String singleBiome = ModConfig.super.singleBiome();
    public String seed = ModConfig.super.seed();
    public boolean generateStructures = ModConfig.super.generateStructures();
    public boolean bonusChest = ModConfig.super.bonusChest();

    @Override
    public String worldName() {
        return this.worldName;
    }

    @Override
    public GameModes gameModes() {
        return this.gameModes;
    }

    @Override
    public Difficulties difficulties() {
        return this.difficulties;
    }

    @Override
    public boolean allowCommands() {
        return this.allowCommands;
    }

    @Override
    public WorldTypes worldTypes() {
        return this.worldTypes;
    }

    @Override
    public List<String> flatLayers() {
        return this.flatLayers;
    }

    @Override
    public String flatBiome() {
        return this.flatBiome;
    }

    @Override
    public String singleBiome() {
        return this.singleBiome;
    }

    @Override
    public String seed() {
        return this.seed;
    }

    @Override
    public boolean generateStructures() {
        return this.generateStructures;
    }

    @Override
    public boolean bonusChest() {
        return this.bonusChest;
    }

    public enum GameModes implements SelectionListEntry.Translatable {
        SURVIVAL("survival"),
        HARDCORE("hardcore"),
        CREATIVE("creative"),
        DEBUG("spectator");

        private final String translate;
        private final String key;

        GameModes(String key) {
            this.translate = "selectWorld.gameMode." + key;
            this.key = key;
        }

        public @NotNull String getName() {
            return this.key;
        }

        @Override
        public @NotNull String getKey() {
            return this.translate;
        }
    }

    public enum Difficulties implements SelectionListEntry.Translatable {
        EASY("easy"),
        NORMAL("normal"),
        HARD("hard"),
        PEACEFUL("peaceful");

        private final String translate;
        private final String key;

        Difficulties(String key) {
            this.translate = "options.difficulty." + key;
            this.key = key;
        }

        public @NotNull String getName() {
            return this.key;
        }

        @Override
        public @NotNull String getKey() {
            return this.translate;
        }
    }

    public enum WorldTypes implements SelectionListEntry.Translatable {
        //~ if >=1.19.3 'default' -> 'normal'
        DEFAULT("normal"),
        SUPERFLAT("flat"),
        AMPLIFIED("amplified"),
        LARGE_BIOMES("large_biomes"),
        SINGLE_BIOME("single_biome_surface"),
        //? <1.19.4 {
        /*SINGLE_BIOME_CAVES("single_biome_caves"),
        SINGLE_BIOME_FLOATING_ISLANDS("single_biome_floating_islands"),*///?}
        DEBUG("debug_all_block_states");

        private final String translate;

        WorldTypes(String key) {
            this.translate = "generator." + /*? >=1.19.4 >>+ '+'*/ "minecraft." + key;
        }

        @Override
        public @NotNull String getKey() {
            return this.translate;
        }
    }
}
