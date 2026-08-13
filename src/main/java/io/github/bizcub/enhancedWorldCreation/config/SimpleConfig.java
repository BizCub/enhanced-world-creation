package io.github.bizcub.enhancedWorldCreation.config;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.simpleConfigLib.autoconfig.ConfigHolder;
import io.github.bizcub.simpleConfigLib.autoconfig.annotation.*;

import java.util.List;

@AutoConfig(name = Main.MOD_ID, translate = true)
public class SimpleConfig implements Config {

    public static ConfigHolder<SimpleConfig> getInstance() {
        return ConfigHolder.register(SimpleConfig.class);
    }

    public String worldName = Config.super.worldName();

    @EnumConfig(translate = true)
    public GameModes gameModes = Config.super.gameModes();

    @EnumConfig(translate = true)
    public Difficulties difficulties = Config.super.difficulties();

    public boolean allowCommands = Config.super.allowCommands();

    @EnumConfig(translate = true)
    public WorldTypes worldTypes = Config.super.worldTypes();

    public List<String> flatLayers = Config.super.flatLayers();

    public String flatBiome = Config.super.flatBiome();

    public String singleBiome = Config.super.singleBiome();

    public String seed = Config.super.seed();

    public boolean generateStructures = Config.super.generateStructures();

    public boolean bonusChest = Config.super.bonusChest();

    @Tooltip
    public boolean decoration = Config.super.decoration();

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

    @Override
    public boolean decoration() {
        return this.decoration;
    }
}
