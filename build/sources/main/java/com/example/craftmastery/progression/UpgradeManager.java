package com.example.craftmastery.progression;

import java.util.HashMap;
import java.util.Map;

public class UpgradeManager {
    private static UpgradeManager instance;
    private Map<String, Upgrade> upgrades;

    private UpgradeManager() {
        upgrades = new HashMap<>();
        registerUpgrades();
    }

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    private void registerUpgrades() {
        addUpgrade(new Upgrade("faster_crafting", "Faster Crafting", 5,
                progression -> {/* Implement faster crafting logic */}));
        addUpgrade(new Upgrade("extra_output", "Extra Output", 10,
                progression -> {/* Implement extra output logic */}));
        // Add more upgrades as needed
    }

    public void addUpgrade(Upgrade upgrade) {
        upgrades.put(upgrade.getId(), upgrade);
    }

    public Upgrade getUpgrade(String id) {
        return upgrades.get(id);
    }
}
