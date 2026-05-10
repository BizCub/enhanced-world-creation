//? forge {
/*package com.bizcub.enhancedWorldCreation.platform;

import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.config.Compat;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(Main.MOD_ID)
public class Forge {

    public Forge() {
        Main.init();

        //? is_cloth_config_available {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
                        Compat.getScreen(screen)
        ));//?}
    }
}*///?}
