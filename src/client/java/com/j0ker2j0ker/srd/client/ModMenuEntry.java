package com.j0ker2j0ker.srd.client;

import com.j0ker2j0ker.srd.client.screen.SrdConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntry implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SrdConfigScreen::new;
    }
}