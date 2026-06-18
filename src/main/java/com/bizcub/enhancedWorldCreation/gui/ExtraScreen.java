package com.bizcub.enhancedWorldCreation.gui;

//? >=1.19.3 {
import com.bizcub.enhancedWorldCreation.Main;
//? >=1.20 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else
//import com.mojang.blaze3d.vertex.PoseStack;
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
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper helper = this.gridLayout.createRowHelper(1);
        helper.addChild(new StringWidget(Component.translatable("enhanced_world_creation.extra.icon"), this.font));
        this.iconPathBox = helper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.empty()));
        helper.addChild(new StringWidget(Component.translatable("enhanced_world_creation.extra.rp"), this.font));
        this.resourcePackPathBox = helper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.empty()));
        helper.addChild(new StringWidget(Component.empty(), this.font));
        this.tip = this.addRenderableWidget(new MultiLineTextWidget(this.width / 2 - 110, this.height - 75, Component.translatable("enhanced_world_creation.extra.on_files_drop"), this.font));
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
    public void extractRenderState(/*? >=1.20 {*/ GuiGraphicsExtractor /*?} else {*/ /*PoseStack *//*?}*/ guiGraphics, int i, int j, float f) {
        //? <1.20.5
        //this.renderBackground(guiGraphics /*? >=1.20.2 {*/, i, j, f/*?}*/);
        super.extractRenderState(guiGraphics, i, j, f);//~}
    }

    @Override
    public void onFilesDrop(List<Path> files) {
        File file = files.get(0).toFile();

        if (file.isFile()) {
            var list = files.get(0).toString().split("\\.");
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
        this.tip.setMessage(Component.translatable("enhanced_world_creation.extra.rp.warning"));
        refreshWidgets();
        this.resourcePackPathBox.setValue(value);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }
}

//?} else {
/*import com.bizcub.enhancedWorldCreation.Main;
import com.bizcub.enhancedWorldCreation.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ExtraScreen extends Screen {
    private final Screen parent;
    private EditBox iconPathBox;
    private EditBox resourcePackPathBox;
    private MultiLineLabel tip;

    public ExtraScreen(Screen parent) {
        super(Utils.getComponent());
        this.parent = parent;
    }

    @Override
    protected void init() {
        Padding padding = new Padding(25, this.height / 2 - 90);

        this.iconPathBox = addWidget(new EditBox(this.font, this.width / 2 - 75, padding.getY(2), 150, 20, Utils.getComponent()));
        this.iconPathBox.setValue(Main.iconPath);
        this.iconPathBox.setMaxLength(1000);

        this.resourcePackPathBox = addWidget(new EditBox(this.font, this.width / 2 - 75, padding.getY(4), 150, 20, Utils.getComponent()));
        this.resourcePackPathBox.setValue(Main.resourcePackPath);
        this.resourcePackPathBox.setMaxLength(1000);

        this.tip = MultiLineLabel.create(
                this.font,
                Utils.getComponent(
                        this.resourcePackPathBox.getValue().isEmpty()
                                ? "enhanced_world_creation.extra.on_files_drop"
                                : "enhanced_world_creation.extra.rp.warning",
                        Utils.ComponentTypes.TRANSLATABLE
                ),
                this.width / 2 - 50, padding.getY(6)
        );

        //~ if >=1.17 'addButton' -> 'addRenderableWidget'
        this.addRenderableWidget(Utils.getButton(this.width / 2 - 75, this.height - 28, 150, 20, CommonComponents.GUI_DONE, (button) -> onClose()));
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        Padding padding = new Padding(25, this.height / 2 - 90);

        this.iconPathBox.render(poseStack, i, j, f);
        this.resourcePackPathBox.render(poseStack, i, j, f);
        drawCenteredString(poseStack, this.font, Utils.getComponent("enhanced_world_creation.extra.icon", Utils.ComponentTypes.TRANSLATABLE), this.width / 2, padding.getY(1) + 5, 16777215);
        drawCenteredString(poseStack, this.font, Utils.getComponent("enhanced_world_creation.extra.rp", Utils.ComponentTypes.TRANSLATABLE), this.width / 2, padding.getY(3) + 5, 16777215);

        this.tip.renderCentered(poseStack, this.width / 2, padding.getY(6));
        super.render(poseStack, i, j, f);
    }

    @Override
    public void onFilesDrop(List<Path> files) {
        File file = files.get(0).toFile();

        if (file.isFile()) {
            String[] list = files.get(0).toString().split("\\.");
            String extension = list[list.length - 1];

            switch (extension) {
                case "zip":
                    addResourcePack(file.getAbsolutePath());
                    break;
                case "png":
                    this.iconPathBox.setValue(file.getAbsolutePath());
                    break;
            }
        } else {
            addResourcePack(file.getAbsolutePath());
        }
        Main.iconPath = this.iconPathBox.getValue();
        Main.resourcePackPath = this.resourcePackPathBox.getValue();
    }

    private void addResourcePack(String value) {
        this.tip = MultiLineLabel.create(
                this.font,
                Utils.getComponent("enhanced_world_creation.extra.rp.warning", Utils.ComponentTypes.TRANSLATABLE),
                this.width / 2 - 50, new Padding(25, this.height / 2 - 90).getY(6)
        );
        this.resourcePackPathBox.setValue(value);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    public static class Padding {
        private final int size;
        private final int defaultY;

        public Padding(int size, int defaultY) {
            this.size = size;
            this.defaultY = defaultY;
        }

        public int getY(int padding) {
            return this.size * padding + this.defaultY;
        }
    }
}*///?}
