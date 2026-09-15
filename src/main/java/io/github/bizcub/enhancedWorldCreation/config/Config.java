package io.github.bizcub.enhancedWorldCreation.config;

import io.github.bizcub.enhancedWorldCreation.Utils;

import java.util.ArrayList;
import java.util.List;

public interface Config {
    static Config get() {
        return Holder.INSTANCE;
    }

    static void set(final Config config) {
        if (config != null) {
            Holder.INSTANCE = config;
        }
    }

    class Holder {
        private static Config INSTANCE = new Config() { };
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
