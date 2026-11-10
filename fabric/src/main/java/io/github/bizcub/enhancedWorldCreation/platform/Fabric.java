package io.github.bizcub.enhancedWorldCreation.platform;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Fabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Main.init(FabricLoader.getInstance().isModLoaded(ConfigHelper.SIMPLE_CONFIG_MOD_ID));
    }

    public static class ModMenu implements ModMenuApi {

        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
            return ConfigHelper::getScreen;
        }
    }
}
