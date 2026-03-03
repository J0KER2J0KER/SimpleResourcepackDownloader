package com.j0ker2j0ker.srd.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SrdClient implements ClientModInitializer {

    public static final String MOD_ID = "srd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Path resourcepack_locations;

    @Override
    public void onInitializeClient() {
    }
}
