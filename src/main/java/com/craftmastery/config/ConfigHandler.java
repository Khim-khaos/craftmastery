package com.craftmastery.config;

import com.craftmastery.CraftMastery;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    public static Configuration config;

    // General settings
    private static int maxSpecializations;
    private static int baseExperiencePerAction;
    private static int baseLearningPointsPerLevel;

    // Specializations
    private static Map<String, String> specializations;

    // Recipe types
    private static String[] recipeTypes;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    private static void loadConfig() {
        config.load();

        // Load general settings
        maxSpecializations = config.getInt("maxSpecializations", "general", 3, 1, 10, "Maximum number of specializations a player can have");
        baseExperiencePerAction = config.getInt("baseExperiencePerAction", "general", 1, 1, 100, "Base experience gained per action (breaking blocks, crafting, etc.)");
        baseLearningPointsPerLevel = config.getInt("baseLearningPointsPerLevel", "general", 1, 1, 10, "Base learning points gained per level");

        // Load specializations
        String[] specializationArray = config.getStringList("specializations", "specializations",
                new String[]{"mining|Mining|Specialization for mining and ore processing",
                        "crafting|Crafting|Specialization for advanced crafting recipes",
                        "farming|Farming|Specialization for farming and animal husbandry"},
                "List of specializations in the format: id|name|description");

        specializations = new HashMap<>();
        for (String spec : specializationArray) {
            String[] parts = spec.split("\\|");
            if (parts.length >= 3) {
                specializations.put(parts[0], parts[1] + "|" + parts[2]);
            }
        }

        // Load recipe types
        recipeTypes = config.getStringList("recipeTypes", "recipes",
                new String[]{"normal", "magic", "tech", "magitech"},
                "List of recipe types");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(CraftMastery.MODID)) {
            loadConfig();
        }
    }

    public static int getMaxSpecializations() {
        return maxSpecializations;
    }

    public static int getBaseExperiencePerAction() {
        return baseExperiencePerAction;
    }

    public static int getBaseLearningPointsPerLevel() {
        return baseLearningPointsPerLevel;
    }

    public static Map<String, String> getSpecializations() {
        return specializations;
    }

    public static String[] getRecipeTypes() {
        return recipeTypes;
    }

    // Add more getter methods for other config options as needed

    public static void setRecipeLearningCost(String recipeId, int cost) {
        config.get("recipeCosts", recipeId, cost).set(cost);
        config.save();
    }

    public static int getRecipeLearningCost(String recipeId) {
        return config.getInt(recipeId, "recipeCosts", 1, 1, 100, "Learning cost for recipe: " + recipeId);
    }

    public static void addSpecialization(String id, String name, String description) {
        specializations.put(id, name + "|" + description);
        String[] updatedSpecs = specializations.entrySet().stream()
                .map(entry -> entry.getKey() + "|" + entry.getValue())
                .toArray(String[]::new);
        config.get("specializations", "specializations", new String[]{}, "List of specializations").set(updatedSpecs);
        config.save();
    }

    public static void removeSpecialization(String id) {
        specializations.remove(id);
        String[] updatedSpecs = specializations.entrySet().stream()
                .map(entry -> entry.getKey() + "|" + entry.getValue())
                .toArray(String[]::new);
        config.get("specializations", "specializations", new String[]{}, "List of specializations").set(updatedSpecs);
        config.save();
    }

    public static void addRecipeType(String recipeType) {
        String[] updatedTypes = new String[recipeTypes.length + 1];
        System.arraycopy(recipeTypes, 0, updatedTypes, 0, recipeTypes.length);
        updatedTypes[recipeTypes.length] = recipeType;
        config.get("recipes", "recipeTypes", new String[]{}, "List of recipe types").set(updatedTypes);
        recipeTypes = updatedTypes;
        config.save();
    }

    public static void removeRecipeType(String recipeType) {
        String[] updatedTypes = new String[recipeTypes.length - 1];
        int index = 0;
        for (String type : recipeTypes) {
            if (!type.equals(recipeType)) {
                updatedTypes[index++] = type;
            }
        }
        config.get("recipes", "recipeTypes", new String[]{}, "List of recipe types").set(updatedTypes);
        recipeTypes = updatedTypes;
        config.save();
    }
}
