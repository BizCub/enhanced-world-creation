package com.bizcub.enhancedWorldCreation.mixin;

import org.spongepowered.asm.mixin.Mixin;

//? >=1.19.3 {
/*import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.gui.ExtraScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreateWorldScreen.GameTab.class)
public class GameTabMixin {

    @ModifyVariable(method = "<init>", at = @At("TAIL"), ordinal = 0)
    private GridLayout.RowHelper screenInit(GridLayout.RowHelper helper) {
        Main.iconPath = "";
        Main.resourcePackPath = "";

        Button button = Button.builder(Component.translatable("enhanced_world_creation.extra.button"), b ->
                Minecraft.getInstance().setScreen(new ExtraScreen(Minecraft.getInstance().screen))).width(210).build();

        helper.addChild(button);
        return helper;
    }
}

*///?} else {
@Mixin(value = {})
public class GameTabMixin {

}//?}
