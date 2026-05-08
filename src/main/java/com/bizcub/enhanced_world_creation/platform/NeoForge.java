//? neoforge {
/*package com.bizcub.enhanced_world_creation.platform;

import com.bizcub.enhanced_world_creation.Main;
import com.bizcub.enhanced_world_creation.config.Compat;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Main.MOD_ID)
public class NeoForge {

    public NeoForge() {
        Main.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class, () -> (minecraft, screen) ->
                        Compat.getScreen(screen)
        );
    }
}*///?}
