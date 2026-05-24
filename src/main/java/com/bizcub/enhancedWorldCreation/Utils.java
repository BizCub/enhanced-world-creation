package com.bizcub.enhancedWorldCreation;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
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
        switch (type) {
            case EMPTY:
                return new TextComponent("");
            case TRANSLATABLE:
                return new TranslatableComponent(text);
            default:
                return new TextComponent(text);
        }
    }

    public static ResourceLocation getDefaultId(String id) {
        /*? >=1.21 {*/ /*return ResourceLocation.withDefaultNamespace(id);
         *//*?} else*/ return new ResourceLocation(id);
    }

    public static Button getButton(int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        return new Button(x, y, width, height, component, onPress);
    }

    public static Biome getBiomeById(String biomeId, RegistryAccess.RegistryHolder registryHolder) {
        Registry<Biome> biomeRegistry = registryHolder.registryOrThrow(Registry.BIOME_REGISTRY);
        return biomeRegistry.getOrThrow(Main.BIOMES.get(biomeId));
    }

    public static Block getBlockById(String blockId) {
        return Main.BLOCKS.get(blockId);
    }

    public static List<FlatLayerInfo> getFlatLayers() {
        List<FlatLayerInfo> layers = new ArrayList<>();
        Main.getConfig().flatLayers().forEach(flatLayer -> {
            boolean isHaveStar = flatLayer.split("\\*").length >= 2;
            List<String> layerSplit = Arrays.asList(flatLayer.split("\\*"));
            int blockCount = isHaveStar ? Integer.parseInt(layerSplit.get(0)) : 1;
            String layer = isHaveStar ? layerSplit.get(1) : flatLayer;
            layers.add(new FlatLayerInfo(blockCount, getBlockById(layer)));
        });
        Collections.reverse(layers);
        return layers;
    }

    public static <T extends ChunkGenerator> WorldGenSettings getSettings(T value, WorldGenSettings currentSettings, RegistryAccess.RegistryHolder registryHolder) {
        return new WorldGenSettings(
                currentSettings.seed(),
                currentSettings.generateFeatures(),
                currentSettings.generateBonusChest(),
                WorldGenSettings.withOverworld(
                        registryHolder.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                        currentSettings.dimensions(),
                        value
                )
        );
    }

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
