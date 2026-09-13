package io.github.bizcub.enhancedWorldCreation.config;

import io.github.bizcub.enhancedWorldCreation.Utils;
import io.github.bizcub.simpleConfigLib.autoconfig.ConfigProvider;

import java.util.ArrayList;
import java.util.List;

public interface Config {
    static Config get() {
        return ConfigProvider.get(Config.class);
    }
    static void set(Config instance) {
        ConfigProvider.set(Config.class, instance);
    }

    default String worldName() {
        return Utils.getComponent("selectWorld.newWorld", Utils.ComponentTypes.TRANSLATABLE).getString();
    }

    default GameModes gameModes() {
        return GameModes.SURVIVAL;
    }

    default Difficulties difficulties() {
        return Difficulties.NORMAL;
    }

    default boolean allowCommands() {
        return false;
    }

    default WorldTypes worldTypes() {
        return WorldTypes.DEFAULT;
    }

    default List<String> flatLayers() {
        return new ArrayList<>();
    }

    default String flatBiome() {
        return "plains";
    }

    default String singleBiome() {
        return "plains";
    }

    default String seed() {
        return "";
    }

    default boolean generateStructures() {
        return true;
    }

    default boolean bonusChest() {
        return false;
    }

    default boolean decoration() {
        return false;
    }
}
