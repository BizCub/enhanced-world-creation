package io.github.bizcub.enhancedWorldCreation.config;

public enum GameModes {
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

    public String getName() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.translate;
    }
}
