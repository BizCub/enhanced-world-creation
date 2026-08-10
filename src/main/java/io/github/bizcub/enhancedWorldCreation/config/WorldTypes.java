package io.github.bizcub.enhancedWorldCreation.config;

public enum WorldTypes {
    DEFAULT("normal"),
    SUPERFLAT("flat"),
    AMPLIFIED("amplified"),
    LARGE_BIOMES("large_biomes"),
    /*? >=26.2*/ FLAT_ALL_DIMENSIONS("flat_all_dimensions"),
    SINGLE_BIOME("single_biome_surface"),
    DEBUG("debug_all_block_states");

    private final String translate;

    WorldTypes(String key) {
        this.translate = "generator.minecraft." + key;
    }

    public String getKey() {
        return this.translate;
    }
}
