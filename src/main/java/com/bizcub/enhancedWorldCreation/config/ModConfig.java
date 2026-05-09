package com.bizcub.enhancedWorldCreation.config;

import net.minecraft.network.chat.Component;

public interface ModConfig {
    ModConfig CONFIG = Compat.isClothConfigLoaded() ? ModClothConfig.getInstance() : new ModConfig() { };

    default String worldName() {
        return Component.translatable("selectWorld.newWorld").getString();
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
