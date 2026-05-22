package com.bizcub.enhancedWorldCreation.mixin;

import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.config.Compat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Unique String pathToSavesFolder = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/saves/";

    @Shadow @Final private WorldCreationUiState uiState;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void screenInit(CallbackInfo ci) {
        if (!Compat.isClothConfigLoaded()) return;

        Map<String, WorldCreationUiState.WorldTypeEntry> worldPresets = new HashMap<>();
        uiState.getAltPresetList().forEach(preset ->
                worldPresets.put(preset.describePreset().getContents().toString().split("'")[1], preset));

        uiState.setName(Main.getConfig().worldName());
        uiState.setGameMode(Main.GAME_MODES.get(Main.getConfig().gameModes().getName()));
        uiState.setDifficulty(Difficulty.byName(Main.getConfig().difficulties().getName()));
        //~ if >=1.21 'setAllowCheats' -> 'setAllowCommands'
        uiState.setAllowCommands(Main.getConfig().allowCommands());

        uiState.setWorldType(worldPresets.get(Main.getConfig().worldTypes().getKey()));
        uiState.setSeed(Main.getConfig().seed());
        uiState.setGenerateStructures(Main.getConfig().generateStructures());
        uiState.setBonusChest(Main.getConfig().bonusChest());

        if (uiState.getSettings().selectedDimensions().overworld() instanceof FlatLevelSource flatLevelSource) {
            FlatLevelGeneratorSettings settings = flatLevelSource.settings();

            var layersInfo = settings.getLayersInfo();
            layersInfo.clear();
            List<String> list = new ArrayList<>(Main.getConfig().flatLayers());
            Collections.reverse(list);
            list.forEach(flatLayer -> {
                boolean isHaveStar = flatLayer.split("\\*").length >= 2;
                List<String> layerSplit = List.of(flatLayer.split("\\*"));
                int blockCount = isHaveStar ? Integer.parseInt(layerSplit.get(0)) : 1;
                String layer = isHaveStar ? layerSplit.get(1) : flatLayer;
                layersInfo.add(new FlatLayerInfo(blockCount, getBlockById(layer)));
            });

            uiState.updateDimensions(PresetEditor.flatWorldConfigurator(settings.withBiomeAndLayers(
                    layersInfo,
                    settings.structureOverrides(),
                    getBiomeById(Main.getConfig().flatBiome())
            )));
        } else {
            uiState.updateDimensions(PresetEditor.fixedBiomeConfigurator(getBiomeById(Main.getConfig().singleBiome())));
        }
    }

    @Unique
    private Holder<Biome> getBiomeById(String biomeId) {
        RegistryAccess registryAccess = uiState.getSettings().worldgenLoadContext();
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, Main.getDefaultId(biomeId));
        //~ if >=1.21.2 'registryOrThrow' -> 'lookupOrThrow'
        Registry<Biome> biomes = registryAccess.lookupOrThrow(Registries.BIOME);
        //~ if >=1.21.2 'getHolderOrThrow' -> 'getOrThrow'
        return biomes.getOrThrow(biomeKey);
    }

    @Unique
    private Block getBlockById(String blockId) {
        RegistryAccess registryAccess = uiState.getSettings().worldgenLoadContext();
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Main.getDefaultId(blockId));
        //~ if >=1.21.2 'registryOrThrow' -> 'lookupOrThrow'
        Registry<Block> blocks = registryAccess.lookupOrThrow(Registries.BLOCK);
        //~ if >=1.21.2 'get' -> 'getValue'
        return blocks.getValue(blockKey);
    }

    @Inject(method = "createNewWorld", at = @At("HEAD"))
    private void createRPFolder(/*? >=1.21.2 {*/ CallbackInfoReturnable<Boolean> cir /*?} else {*/ /*CallbackInfo ci *//*?}*/) {
        new File(pathToSavesFolder + uiState.getTargetFolder() + "/resourcepacks").mkdirs();
    }

    @Inject(method = "createNewWorld", at = @At("TAIL"))
    private void copyResources(/*? >=1.21.2 {*/ CallbackInfoReturnable<Boolean> cir /*?} else {*/ /*CallbackInfo ci *//*?}*/) throws IOException {
        String pathToWorld = pathToSavesFolder;

        if (!Main.iconPath.isEmpty()) {
            try {
                FileUtils.copyFile(
                        new File(Main.iconPath),
                        new File(pathToWorld + uiState.getTargetFolder() + LevelResource.ICON_FILE)
                );
                BufferedImage originalImage = ImageIO.read(new File(Main.iconPath));
                BufferedImage resizedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();

                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(originalImage, 0, 0, 64, 64, null);
                g.dispose();
                ImageIO.write(resizedImage, "png", new File(pathToWorld + uiState.getTargetFolder() + LevelResource.ICON_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Main.resourcePackPath.isEmpty()) {
            String inputPath = Main.resourcePackPath;
            String outputPath = pathToWorld + uiState.getTargetFolder() + LevelResource.MAP_RESOURCE_FILE;

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
}
