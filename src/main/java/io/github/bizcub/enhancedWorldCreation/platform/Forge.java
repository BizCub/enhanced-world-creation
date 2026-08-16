//? forge {
/*package io.github.bizcub.enhancedWorldCreation.platform;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(Main.MOD_ID)
public class Forge {

    public Forge() {
        Main.init();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> ConfigHelper.getScreen(screen)));
    }
}*///?}
