//? fabric {
package com.bizcub.template.platform;

import com.bizcub.template.Main;
import net.fabricmc.api.ClientModInitializer;

public class Fabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Main.init();
    }
}//?}
