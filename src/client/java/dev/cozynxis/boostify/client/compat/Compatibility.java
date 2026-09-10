package dev.cozynxis.boostify.client.compat;

import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

public final class Compatibility {
    public static boolean sodium;
    public static boolean lithium;
    public static boolean ferriteCore;
    public static boolean immediatelyFast;
    public static boolean entityCulling;
    public static boolean modMenu;

    private Compatibility() {}

    public static void detect() {
        FabricLoader loader = FabricLoader.getInstance();
        sodium = loader.isModLoaded("sodium");
        lithium = loader.isModLoaded("lithium");
        ferriteCore = loader.isModLoaded("ferritecore");
        immediatelyFast = loader.isModLoaded("immediatelyfast");
        entityCulling = loader.isModLoaded("entityculling");
        modMenu = loader.isModLoaded("modmenu");
    }

    public static boolean shouldRunEntityCulling() {
        return !entityCulling;
    }

    public static String summary() {
        List<String> mods = new ArrayList<>();
        if (sodium) mods.add("Sodium");
        if (lithium) mods.add("Lithium");
        if (ferriteCore) mods.add("FerriteCore");
        if (immediatelyFast) mods.add("ImmediatelyFast");
        if (entityCulling) mods.add("EntityCulling");
        if (modMenu) mods.add("Mod Menu");
        return mods.isEmpty() ? "standalone" : String.join(", ", mods);
    }
}
