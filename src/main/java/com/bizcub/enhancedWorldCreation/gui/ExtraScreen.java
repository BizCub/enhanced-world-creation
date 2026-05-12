package com.bizcub.enhancedWorldCreation.gui;

import com.bizcub.enhancedWorldCreation.Main;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
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
    private final GridLayout gridLayout = new GridLayout();
    private EditBox iconPathBox;
    private EditBox resourcePackPathBox;
    private MultiLineTextWidget tip;

    public ExtraScreen(Screen parent) {
        super(Component.literal(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper helper = this.gridLayout.createRowHelper(1);
        helper.addChild(new StringWidget(Component.literal("icon"), this.font));
        this.iconPathBox = helper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.empty()));
        helper.addChild(new StringWidget(Component.literal("rp"), this.font));
        this.resourcePackPathBox = helper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.empty()));
        helper.addChild(new StringWidget(Component.empty(), this.font));
        this.tip = this.addRenderableWidget(new MultiLineTextWidget(this.width / 2 - 110, this.height - 75, Component.empty(), this.font));
        this.tip.setCentered(true).setMaxWidth(250);
        refreshWidgets();

        this.iconPathBox.setMaxLength(1000);
        this.resourcePackPathBox.setMaxLength(1000);

        this.iconPathBox.setValue(Main.iconPath);
        this.resourcePackPathBox.setValue(Main.resourcePackPath);

        HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
        AbstractWidget doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose()).pos(this.width / 2 - 75, this.height - 28).size(150, 20).build();
        layout.addToFooter(doneButton);
        layout.visitWidgets(this::addRenderableWidget);
    }
    
    private void refreshWidgets() {
        this.gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(this.gridLayout, 0, 0, this.width, this.height, 0.5f, 0.5f);
        this.gridLayout.visitWidgets(this::addRenderableWidget);
    }

    @Override //~ if >=26.1 'render(' -> 'extractRenderState(' {
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int i, int j, float f) {
        //? <1.20.5
        //this.renderBackground(guiGraphics /*? >=1.20.2 {*/, i, j, f/*?}*/);
        super.extractRenderState(guiGraphics, i, j, f);//~}
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
        Main.iconPath = iconPathBox.getValue();
        Main.resourcePackPath = resourcePackPathBox.getValue();
    }

    private void addResourcePack(String value) {
        this.tip.setMessage(Component.literal("For the resource pack to start working, you need to re-enter the world after creation"));
        refreshWidgets();
        this.resourcePackPathBox.setValue(value);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
