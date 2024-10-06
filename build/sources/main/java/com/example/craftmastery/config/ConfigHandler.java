package com.example.craftmastery.config;

import com.example.craftmastery.CraftMastery;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    // Config categories

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_INTEGRATION = "integration";

    // Config options
    public static boolean enableJEIIntegration;
    public static String unknownRecipeMessage;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    private static void loadConfig() {
        enableJEIIntegration = config.getBoolean("enableJEIIntegration", CATEGORY_INTEGRATION, true, "Enable integration with Just Enough Items (JEI)");        unknownRecipeMessage = config.getString("unknownRecipeMessage", CATEGORY_GENERAL, "I don't know how this works", "Message displayed when a player tries to use an unknown recipe");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @Mod.EventBusSubscriber(modid = CraftMastery.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(CraftMastery.MODID)) {
                loadConfig();
            }
        }
    }
}
