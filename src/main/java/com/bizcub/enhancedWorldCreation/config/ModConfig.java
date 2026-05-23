package com.bizcub.enhancedWorldCreation.config;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public interface ModConfig {
    ModConfig CONFIG = Compat.isClothConfigLoaded() ? ModClothConfig.getInstance() : new ModConfig() { };

    //~ if >=1.19.3 'new TranslatableComponent' -> 'Component.translatable'
    default String worldName() {
        return new TranslatableComponent("selectWorld.newWorld").getString();
    }

    default ModClothConfig.GameModes gameModes() {
        return ModClothConfig.GameModes.SURVIVAL;
    }

    default ModClothConfig.Difficulties difficulties() {
        return ModClothConfig.Difficulties.NORMAL;
    }

    default boolean allowCommands() {
        return false;
    }

    default ModClothConfig.WorldTypes worldTypes() {
        return ModClothConfig.WorldTypes.DEFAULT;
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
}
