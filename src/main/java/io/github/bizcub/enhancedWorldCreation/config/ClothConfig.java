package io.github.bizcub.enhancedWorldCreation.config;

import io.github.bizcub.enhancedWorldCreation.Main;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.List;

@me.shedaniel.autoconfig.annotation.Config(name = Main.MOD_ID)
public class ClothConfig implements Config, ConfigData {

    public static ClothConfig getInstance() {
        return AutoConfig.register(ClothConfig.class, GsonConfigSerializer::new).getConfig();
    }

    public String worldName = Config.super.worldName();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GameModes gameModes = Config.super.gameModes();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Difficulties difficulties = Config.super.difficulties();

    public boolean allowCommands = Config.super.allowCommands();

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public WorldTypes worldTypes = Config.super.worldTypes();

    public List<String> flatLayers = Config.super.flatLayers();

    public String flatBiome = Config.super.flatBiome();

    public String singleBiome = Config.super.singleBiome();

    public String seed = Config.super.seed();

    public boolean generateStructures = Config.super.generateStructures();

    public boolean bonusChest = Config.super.bonusChest();

    @ConfigEntry.Gui.Tooltip
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
