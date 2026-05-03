package com.j0ker2j0ker.srd.client;

import com.j0ker2j0ker.srd.client.screen.SrdConfigScreen;
import com.j0ker2j0ker.srd.client.util.SrdConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class SrdClient implements ClientModInitializer {

    public static final String MOD_ID = "srd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SrdConfig CONFIG;
    public static SrdClient INSTANCE;

    public static Path resourcepack_locations;

    @Override
    public void onInitializeClient() {
        CONFIG = SrdConfig.load();
        INSTANCE = this;

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            CONFIG.save();
        });
        ClientPlayConnectionEvents.JOIN.register((clientPacketListener, packetSender, minecraft) -> {
            SrdClient.INSTANCE.saveResourcePack();
        });

        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("srd")
                            .then(literal("config")
                                    .executes(ctx -> {
                                        Minecraft.getInstance().execute(() ->
                                                Minecraft.getInstance().setScreen(
                                                        new SrdConfigScreen(Minecraft.getInstance().screen)
                                                )
                                        );
                                        return 1;
                                    }))
            );
        });
    }

    public void saveResourcePack() {
        if(Minecraft.getInstance().getCurrentServer() != null && Minecraft.getInstance().getCurrentServer().getResourcePackStatus().name().equalsIgnoreCase("ENABLED")) {
            Path targetDir = Minecraft.getInstance().getResourcePackDirectory();
            if(!Files.exists(targetDir)) {
                try {
                    Files.createDirectories(targetDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            targetDir = targetDir.resolve(Minecraft.getInstance().getCurrentServer().ip.replaceAll("[\\\\/:*?\"<>|]", "_") + "_" + SrdClient.resourcepack_locations.getFileName().toString() + ".zip");
            try {
                Files.copy(SrdClient.resourcepack_locations, targetDir, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
