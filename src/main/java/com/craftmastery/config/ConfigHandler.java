package com.craftmastery.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private static Configuration config;

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        loadConfig();
    }

    private static void loadConfig() {
        config.load();
        // Здесь вы можете добавить другие настройки конфигурации
        config.save();
    }

    public static Map<Integer, String> getLevelRewards() {
        Map<Integer, String> rewards = new HashMap<>();
        String[] levelRewards = config.getStringList("LevelRewards", Configuration.CATEGORY_GENERAL,
                new String[] {
                        "5:10,minecraft:diamond",
                        "10:20",
                        "15:30,minecraft:emerald"
                },
                "Rewards for reaching levels. Format: level:learningPoints,itemReward");

        for (String reward : levelRewards) {
            String[] parts = reward.split(":");
            int level = Integer.parseInt(parts[0]);
            rewards.put(level, parts[1]);
        }

        return rewards;
    }

    public static Map<String, String> getSpecializations() {
        Map<String, String> specializations = new HashMap<>();
        String[] specializationConfigs = config.getStringList("Specializations", Configuration.CATEGORY_GENERAL,
                new String[] {
                        "mining:Mining|Improve your mining skills|UNRESTRICTED",
                        "farming:Farming|Become a master farmer|UNRESTRICTED",
                        "crafting:Crafting|Enhance your crafting abilities|RESTRICTED"
                },
                "Specializations configuration. Format: id:name|description|type");

        for (String spec : specializationConfigs) {
            String[] parts = spec.split(":", 2);
            specializations.put(parts[0], parts[1]);
        }

        return specializations;
    }
}
