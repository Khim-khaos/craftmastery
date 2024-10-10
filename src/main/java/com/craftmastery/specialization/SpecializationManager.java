package com.craftmastery.specialization;

import com.craftmastery.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class SpecializationManager {
    private static SpecializationManager instance;
    private Map<String, Specialization> specializations;
    private Map<UUID, Set<String>> playerSpecializations;

    private SpecializationManager() {
        specializations = new HashMap<>();
        playerSpecializations = new HashMap<>();
    }

    public static SpecializationManager getInstance() {
        if (instance == null) {
            instance = new SpecializationManager();
        }
        return instance;
    }

    public void initializeSpecializations() {
        Map<String, String> specializationConfigs = ConfigHandler.getSpecializations();
        for (Map.Entry<String, String> entry : specializationConfigs.entrySet()) {
            String id = entry.getKey();
            String[] data = entry.getValue().split("\\|");
            if (data.length >= 2) {
                String name = data[0];
                String description = data[1];
                Specialization specialization = new Specialization(id, name, description);
                addSpecialization(specialization);
            }
        }
    }

    public void addSpecialization(Specialization specialization) {
        specializations.put(specialization.getId(), specialization);
    }

    public Specialization getSpecialization(String id) {
        return specializations.get(id);
    }

    public void addPlayerSpecialization(EntityPlayer player, String specializationId) {
        UUID playerId = player.getUniqueID();
        playerSpecializations.computeIfAbsent(playerId, k -> new HashSet<>()).add(specializationId);
    }

    public boolean playerHasSpecialization(EntityPlayer player, String specializationId) {
        UUID playerId = player.getUniqueID();
        Set<String> playerSpecs = playerSpecializations.get(playerId);
        return playerSpecs != null && playerSpecs.contains(specializationId);
    }

    public Set<String> getPlayerSpecializations(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        return new HashSet<>(playerSpecializations.getOrDefault(playerId, new HashSet<>()));
    }

    public boolean isRecipeAllowedForPlayer(EntityPlayer player, String recipeId) {
        Set<String> playerSpecs = getPlayerSpecializations(player);
        for (String specId : playerSpecs) {
            Specialization spec = specializations.get(specId);
            if (spec != null && spec.hasRecipe(recipeId)) {
                return true;
            }
        }
        return false;
    }

    public void addRecipeToSpecialization(String specializationId, String recipeId) {
        Specialization spec = specializations.get(specializationId);
        if (spec != null) {
            spec.addRecipe(recipeId);
        }
    }

    public void removeRecipeFromSpecialization(String specializationId, String recipeId) {
        Specialization spec = specializations.get(specializationId);
        if (spec != null) {
            spec.removeRecipe(recipeId);
        }
    }

    public List<Specialization> getAllSpecializations() {
        return new ArrayList<>(specializations.values());
    }

    public void savePlayerSpecializations(EntityPlayer player) {
        // Implement saving player specializations to NBT or database
    }

    public void loadPlayerSpecializations(EntityPlayer player) {
        // Implement loading player specializations from NBT or database
    }

    public boolean canPlayerChooseSpecialization(EntityPlayer player, String specializationId) {
        // Implement logic to check if a player can choose a specialization
        // For example, check if the player has met certain requirements
        return true;
    }

    public void resetPlayerSpecializations(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        playerSpecializations.remove(playerId);
    }

    public int getPlayerSpecializationCount(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        Set<String> playerSpecs = playerSpecializations.get(playerId);
        return playerSpecs != null ? playerSpecs.size() : 0;
    }

    public boolean canPlayerLearnMoreSpecializations(EntityPlayer player) {
        // Implement logic to check if a player can learn more specializations
        // For example, check against a maximum number of specializations
        int maxSpecializations = ConfigHandler.getMaxSpecializations();
        return getPlayerSpecializationCount(player) < maxSpecializations;
    }
}
