package com.bizcub.enhancedWorldCreation;

import net.minecraft.client.gui.components.Button;
/*? >=1.18.2*/ import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
//? >=1.19.4 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;//?}
/*? <1.19 {*/
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;*///?}
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

    public static Component getComponent() {
        return getComponent("", ComponentTypes.EMPTY);
    }

    public static Component getComponent(String text, Utils.ComponentTypes type) {
        //? >=1.19 {
        return switch (type) {
            case EMPTY -> Component.empty();
            case TRANSLATABLE -> Component.translatable(text);
            default -> Component.literal(text);
        };
        //?} else {
        /*switch (type) {
            case EMPTY:
                return new TextComponent("");
            case TRANSLATABLE:
                return new TranslatableComponent(text);
            default:
                return new TextComponent(text);
        }*///?}
    }

    public static Identifier getDefaultId(String id) {
        /*? >=1.21 {*/ return Identifier.withDefaultNamespace(id);
         /*?} else*/ //return new Identifier(id);
    }

    public static Button getButton(int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        /*? >=1.19.3 {*/ return Button.builder(component, onPress).pos(x, y).size(width, height).build();
        /*?} else*/ //return new Button(x, y, width, height, component, onPress);
    }

    //? >=1.19.4 {
    public static Holder<Biome> getBiomeById(String biomeId, RegistryAccess registryAccess) {
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, Utils.getDefaultId(biomeId));
        //~ if >=1.21.2 'registryOrThrow' -> 'lookupOrThrow'
        Registry<Biome> biomes = registryAccess.lookupOrThrow(Registries.BIOME);
        //~ if >=1.21.2 'getHolderOrThrow' -> 'getOrThrow' {
        //~ if >=1.21.2 'get' -> 'getValue'
        Biome biome = biomes.getValue(biomeKey);
        return biome != null
                ? biomes.getOrThrow(biomeKey)
                : biomes.getOrThrow(Biomes.PLAINS);//~}
    }

    public static Block getBlockById(String blockId, RegistryAccess registryAccess) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Utils.getDefaultId(blockId));
        //~ if >=1.21.2 'registryOrThrow' -> 'lookupOrThrow'
        Registry<Block> blocks = registryAccess.lookupOrThrow(Registries.BLOCK);
        //~ if >=1.21.2 'get' -> 'getValue'
        return blocks.getValue(blockKey);
    }

    //?} else {
    /*public static /^? <1.18.2 {^/ /^Biome ^//^?} else {^/ Holder<Biome>  /^?}^/ getBiomeById(String biomeId, RegistryAccess /^? <1.18.2 {^/ /^.RegistryHolder ^//^?}^/ registryAccess) {
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
        //~ if >=1.18.2 'getOrThrow' -> 'getHolderOrThrow' {
        Biome biome = biomeRegistry.get(Main.BIOMES.get(biomeId));
        return biome != null
                ? biomeRegistry.getHolderOrThrow(Main.BIOMES.get(biomeId))
                : biomeRegistry.getHolderOrThrow(Biomes.PLAINS);//~}
    }

    public static Block getBlockById(String blockId) {
        return Main.BLOCKS.get(blockId);
    }*///?}

    public static List<FlatLayerInfo> getFlatLayers(/*? >=1.19.4 >> ')'*/ RegistryAccess registryAccess) {
        List<FlatLayerInfo> layers = new ArrayList<>();
        Main.getConfig().flatLayers().forEach(flatLayer -> {
            boolean isHaveStar = flatLayer.split("\\*").length >= 2;
            List<String> layerSplit = Arrays.asList(flatLayer.split("\\*"));
            int blockCount = isHaveStar ? Integer.parseInt(layerSplit.get(0)) : 1;
            String layer = isHaveStar ? layerSplit.get(1) : flatLayer;
            Block block = getBlockById(layer /*? >=1.19.4 >> ')'*/, registryAccess);
            if (block != null)
                layers.add(new FlatLayerInfo(blockCount, block));
        });
        Collections.reverse(layers);
        return layers;
    }

    //? <1.19.4 {
    /*public static <T extends ChunkGenerator> WorldGenSettings getSettings(T value, WorldGenSettings currentSettings, RegistryAccess /^? <1.18.2 {^/ /^.RegistryHolder ^//^?}^/ registryAccess) {
        return new WorldGenSettings(
                currentSettings.seed(),
                //~ if >=1.19 'generateFeatures' -> 'generateStructures'
                currentSettings.generateStructures(),
                currentSettings.generateBonusChest(),
                WorldGenSettings.withOverworld(
                        registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                        /^? 1.18.2 {^/ /^DimensionType.defaultDimensions(registryAccess, currentSettings.seed()),
                        ^//^?} else^/ currentSettings.dimensions(),
                        value
                )
        );
    }*///?}

    public static void copyResources(String pathToWorld, String worldName) throws IOException {
        if (!Main.iconPath.isEmpty()) {
            try {
                FileUtils.copyFile(
                        new File(Main.iconPath),
                        new File(pathToWorld + worldName + "/icon.png")
                );
                BufferedImage originalImage = ImageIO.read(new File(Main.iconPath));
                BufferedImage resizedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();

                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(originalImage, 0, 0, 64, 64, null);
                g.dispose();
                ImageIO.write(resizedImage, "png", new File(pathToWorld + worldName + "/icon.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Main.resourcePackPath.isEmpty()) {
            String inputPath = Main.resourcePackPath;
            String outputPath = pathToWorld + worldName + LevelResource.MAP_RESOURCE_FILE;

            if (new File(inputPath).isFile()) {
                try {
                    FileUtils.copyFile(new File(Main.resourcePackPath), new File(outputPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Path sourceDir = Paths.get(inputPath);

                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath))) {
                    Files.walk(sourceDir).forEach(path -> {
                        try {
                            String relativePath = sourceDir.relativize(path).toString();
                            relativePath = relativePath.replace("\\", "/");

                            if (Files.isDirectory(path)) {
                                if (!relativePath.isEmpty()) {
                                    ZipEntry dirEntry = new ZipEntry(relativePath + "/");
                                    zos.putNextEntry(dirEntry);
                                    zos.closeEntry();
                                }
                            } else {
                                ZipEntry fileEntry = new ZipEntry(relativePath);
                                zos.putNextEntry(fileEntry);
                                Files.copy(path, zos);
                                zos.closeEntry();
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }
        }
    }

    public enum ComponentTypes {
        EMPTY, LITERAL, TRANSLATABLE
    }
}
