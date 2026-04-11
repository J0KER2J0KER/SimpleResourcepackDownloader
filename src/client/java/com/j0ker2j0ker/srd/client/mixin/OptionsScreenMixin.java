package com.j0ker2j0ker.srd.client.mixin;

import com.j0ker2j0ker.srd.client.SrdClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    Minecraft mc =  Minecraft.getInstance();

    protected OptionsScreenMixin(Component component) {
        super(component);
    }
    @Unique
    private static final Identifier START = Identifier.fromNamespaceAndPath("srd", "icon/start");
    @ModifyArgs(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 6)
    )
    private void wrapResourcePackButton(Args args) {
        LayoutElement originalButton = args.get(0);

        LinearLayout wrapper = LinearLayout.horizontal().spacing(4);

        SpriteIconButton customButton = SpriteIconButton.builder(Component.nullToEmpty(""), (button) -> {
            SrdClient.INSTANCE.saveResourcePack();
            button.setFocused(false);
        }, true).width(20).sprite(START, 16, 16).build();
        if(mc.player == null || mc.level == null || mc.isLocalServer() || mc.getCurrentServer() == null || !mc.getCurrentServer().getResourcePackStatus().name().equalsIgnoreCase("ENABLED")) customButton.active = false;

        if (originalButton instanceof AbstractWidget originalWidget) {
            originalWidget.setWidth(originalWidget.getWidth() - 24);
        }

        wrapper.addChild(customButton);
        wrapper.addChild(originalButton);

        args.set(0, wrapper);
    }
}