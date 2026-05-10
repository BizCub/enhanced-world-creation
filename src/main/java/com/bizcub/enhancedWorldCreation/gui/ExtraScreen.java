package com.bizcub.enhancedWorldCreation.gui;

import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ExtraScreen extends Screen {
    private final Screen parent;
    private EditBox iconPathBox;
    private EditBox resourcePackPathBox;

    public ExtraScreen(Screen parent) {
        super(Component.literal(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper helper = gridLayout.createRowHelper(1);
        helper.addChild(new StringWidget(Component.literal("icon"), this.font));
        this.iconPathBox = helper.addChild(new EditBox(this.font, 150, 20, Component.empty()));
        helper.addChild(new StringWidget(Component.literal("rp"), this.font));
        this.resourcePackPathBox = helper.addChild(new EditBox(this.font, 150, 20, Component.empty()));
        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, 0, 0, this.width, this.height, 0.5f, 0.5f);
        gridLayout.visitWidgets(this::addRenderableWidget);

        this.iconPathBox.setMaxLength(1000);
        this.resourcePackPathBox.setMaxLength(1000);

        this.iconPathBox.setValue(Main.iconPath);
        this.resourcePackPathBox.setValue(Main.resourcePackPath);

        HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
        AbstractWidget doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose()).pos(this.width / 2 - 75, this.height - 28).size(150, 20).build();
        layout.addToFooter(doneButton);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void onFilesDrop(List<Path> files) {
        File file = files.getFirst().toFile();

        if (file.isFile()) {
            var list = files.getFirst().toString().split("\\.");
            String extension = list[list.length - 1];

            switch (extension) {
                case "zip" -> addResourcePack(file.getAbsolutePath());
                case "png" -> this.iconPathBox.setValue(file.getAbsolutePath());
                default -> {}
            }
        } else {
            addResourcePack(file.getAbsolutePath());
        }
    }

    private void addResourcePack(String value) {
        this.resourcePackPathBox.setValue(value);
    }

    @Override
    public void onClose() {
        Main.iconPath = iconPathBox.getValue();
        Main.resourcePackPath = resourcePackPathBox.getValue();
        this.minecraft.setScreen(this.parent);
    }
}
