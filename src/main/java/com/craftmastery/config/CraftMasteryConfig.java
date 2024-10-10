package com.craftmastery.config;

import com.craftmastery.CraftMastery;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CraftMastery.MODID)
public class CraftMasteryConfig {

    @Config.Comment("Experience and points settings")
    public static final Experience EXPERIENCE = new Experience();

    public static class Experience {
        @Config.Comment("Base XP gained from crafting an item")
        @Config.RangeInt(min = 0)
        public int baseCraftingXP = 1;

        @Config.Comment("Base XP gained from mining a block")
        @Config.RangeInt(min = 0)
        public int baseMiningXP = 1;

        @Config.Comment("Learning points gained per level")
        @Config.RangeInt(min = 0)
        public int learningPointsPerLevel = 1;

        @Config.Comment("Craft reset points gained per level")
        @Config.RangeInt(min = 0)
        public int craftResetPointsPerLevel = 1;

        @Config.Comment("Specialization reset points gained per level")
        @Config.RangeInt(min = 0)
        public int specializationResetPointsPerLevel = 1;

        @Config.Comment("Experience multiplier per level")
        @Config.RangeInt(min = 100)
        public int expMultiplierPerLevel = 100;
    }

    @Mod.EventBusSubscriber(modid = CraftMastery.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(CraftMastery.MODID)) {
                ConfigManager.sync(CraftMastery.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
