package com.example.craftmastery.config;

import com.example.craftmastery.CraftMastery;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CraftMastery.MODID)
public class ModConfig {

    @Config.Comment("General settings")
    public static General general = new General();

    @Config.Comment("Progression settings")
    public static Progression progression = new Progression();

    public static class General {
        @Config.Comment("Enable integration with JEI")
        public boolean enableJEIIntegration = true;

        @Config.Comment("Enable tooltip information for items")
        public boolean enableItemTooltips = true;
    }

    public static class Progression {
        @Config.Comment("Base experience gained from crafting")
        @Config.RangeInt(min = 1, max = 1000)
        public int baseCraftingExperience = 10;

        @Config.Comment("Experience multiplier for recipe difficulty")
        @Config.RangeDouble(min = 0.1, max = 10.0)
        public double recipeDifficultyMultiplier = 1.0;

        @Config.Comment("Maximum level cap")
        @Config.RangeInt(min = 1, max = 1000)
        public int maxLevel = 100;

        @Config.Comment("Experience required for first level")
        @Config.RangeInt(min = 1, max = 10000)
        public int baseExperienceRequired = 100;

        @Config.Comment("Experience increase per level")
        @Config.RangeDouble(min = 1.0, max = 2.0)
        public double experienceIncreasePerLevel = 1.1;
    }

    @Mod.EventBusSubscriber(modid = CraftMastery.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(CraftMastery.MODID)) {
                ConfigManager.sync(CraftMastery.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
