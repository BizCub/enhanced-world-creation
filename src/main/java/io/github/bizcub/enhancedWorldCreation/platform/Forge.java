//? forge {
/*package io.github.bizcub.enhancedWorldCreation.platform;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import me.shedaniel.autoconfig.AutoConfig;
/^? >=1.19^/ import net.minecraftforge.client.ConfigScreenHandler;
/^? >=1.18 && <=1.18.2^/ //import net.minecraftforge.client.ConfigGuiHandler;
/^? >=1.17 && <=1.17.1^/ //import net.minecraftforge.fmlclient.ConfigGuiHandler;
/^? <=1.16.5^/ //import net.minecraftforge.fml.ExtensionPoint;
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
