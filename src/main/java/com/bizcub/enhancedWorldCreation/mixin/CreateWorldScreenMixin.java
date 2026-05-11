package com.bizcub.enhancedWorldCreation.mixin;

import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.config.Compat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

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
        uiState.setAllowCommands(Main.getConfig().allowCommands());

        uiState.setWorldType(worldPresets.get(Main.getConfig().worldTypes().getKey()));
        uiState.setSeed(Main.getConfig().seed());
        uiState.setGenerateStructures(Main.getConfig().generateStructures());
        uiState.setBonusChest(Main.getConfig().bonusChest());
    }

    @Inject(method = "createNewWorld", at = @At("HEAD"))
    private void createRPFolder(CallbackInfoReturnable<Boolean> cir) {
        new File(Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/saves/" + uiState.getTargetFolder() + "/resourcepacks").mkdirs();
    }

    @Inject(method = "createNewWorld", at = @At("TAIL"))
    private void copyResources(CallbackInfoReturnable<Boolean> cir) throws IOException {
        String pathToWorld = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/saves/";

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

            System.out.println(new File(outputPath).canWrite());

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
