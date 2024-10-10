package com.craftmastery.specialization;

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
}
