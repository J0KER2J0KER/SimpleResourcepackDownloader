package com.j0ker2j0ker.srd.client.screen;

import com.j0ker2j0ker.srd.client.SrdClient;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SrdConfigScreen extends Screen {
    private final Screen parent;
    private Checkbox autoDownloadCheckbox;

    // layout
    private int centerX;
    private int autoLabelX, autoLabelY;

    // description texts
    private static final List<Component> AUTO_DESC = List.of(
            Component.literal("If enabled, resourcepacks will automatically"),
            Component.literal("be downloaded upon joining a server.")
    );

    public SrdConfigScreen(Screen parent) {
        super(Component.literal("Simple Resourcepack Downloader Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.centerX = this.width / 2;

        // label positions (left side)
        this.autoLabelX = centerX - 180;
        this.autoLabelY = 75;

        int autoCheckboxX = centerX - 20;
        int autoCheckboxY = 70;

        this.autoDownloadCheckbox = Checkbox.builder(Component.empty(), this.font)
                .pos(autoCheckboxX, autoCheckboxY)
                .selected(SrdClient.CONFIG.autoDownload)
                .build();
        this.addRenderableWidget(this.autoDownloadCheckbox);

        this.addRenderableWidget(Button.builder(Component.literal("Save"), b -> {
            SrdClient.CONFIG.autoDownload = this.autoDownloadCheckbox.selected();
            SrdClient.CONFIG.save();
            this.onClose();
        }).pos(centerX - 155, this.height - 50).width(150).build());

        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> this.onClose())
                .pos(centerX + 5, this.height - 50).width(150).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.nextStratum();

        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
        graphics.text(this.font, Component.literal("Automatically download:"), autoLabelX, autoLabelY, 0xFFFFFFFF);

        // hover descriptions (over label text or over input widgets)
        boolean hoverAutoLabel = isHovering(mouseX, mouseY, autoLabelX, autoLabelY, this.font.width("Automatically download:"), 10);
        boolean hoverAutoCheckbox = this.autoDownloadCheckbox != null && this.autoDownloadCheckbox.isMouseOver(mouseX, mouseY);

        if (hoverAutoLabel || hoverAutoCheckbox) {
            graphics.setComponentTooltipForNextFrame(this.font, AUTO_DESC, mouseX, mouseY);
        }
    }

    private boolean isHovering(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}