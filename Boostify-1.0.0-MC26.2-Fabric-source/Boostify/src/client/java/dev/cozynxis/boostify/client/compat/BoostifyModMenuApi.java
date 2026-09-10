package dev.cozynxis.boostify.client.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cozynxis.boostify.client.screen.BoostifyScreen;

public final class BoostifyModMenuApi implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BoostifyScreen::new;
    }
}
