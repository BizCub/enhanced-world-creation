package io.github.bizcub.enhancedWorldCreation.config;

public enum Difficulties {
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

    public String getName() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.translate;
    }
}
