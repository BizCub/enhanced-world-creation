package io.github.bizcub.enhancedWorldCreation.platform;

import io.github.bizcub.enhancedWorldCreation.Main;
import io.github.bizcub.enhancedWorldCreation.config.ConfigHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Main.MOD_ID, dist = Dist.CLIENT)
public class NeoForge {

    public NeoForge(ModContainer modContainer) {
        Main.init(ModList.get().isLoaded(ConfigHelper.SIMPLE_CONFIG_MOD_ID));

        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (container, parent) -> ConfigHelper.getScreen(parent));
    }
}
