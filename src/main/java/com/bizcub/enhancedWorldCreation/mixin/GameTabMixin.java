package com.bizcub.enhancedWorldCreation.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreateWorldScreen.GameTab.class)
public class GameTabMixin {

    @ModifyVariable(method = "<init>", at = @At("TAIL"))
    private GridLayout.RowHelper screenInit(GridLayout.RowHelper helper) {
        Button button = Button.builder(Component.literal("Extra Button"), b -> System.out.println("test")).width(210).build();

        helper.addChild(button);
        return helper;
    }
}
