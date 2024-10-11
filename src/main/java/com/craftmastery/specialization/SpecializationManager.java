package com.craftmastery.specialization;

import com.craftmastery.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

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

    public void savePlayerSpecializations(EntityPlayer player, NBTTagCompound compound) {
        UUID playerId = player.getUniqueID();
        Set<String> playerSpecs = playerSpecializations.get(playerId);
        if (playerSpecs != null) {
            NBTTagList specList = new NBTTagList();
            for (String spec : playerSpecs) {
                specList.appendTag(new NBTTagString(spec));
            }
            compound.setTag("Specializations", specList);
        }
    }

    public void loadPlayerSpecializations(EntityPlayer player, NBTTagCompound compound) {
        UUID playerId = player.getUniqueID();
        Set<String> playerSpecs = new HashSet<>();
        if (compound.hasKey("Specializations", Constants.NBT.TAG_LIST)) {
            NBTTagList specList = compound.getTagList("Specializations", Constants.NBT.TAG_STRING);
            for (int i = 0; i < specList.tagCount(); i++) {
                playerSpecs.add(specList.getStringTagAt(i));
            }
        }
        playerSpecializations.put(playerId, playerSpecs);
    }

    public boolean canPlayerChooseSpecialization(EntityPlayer player, String specializationId) {
        int playerLevel = player.experienceLevel; // You might want to use your own leveling system here
        Specialization spec = specializations.get(specializationId);
        if (spec != null) {
            return playerLevel >= spec.getRequiredLevel() && !playerHasSpecialization(player, specializationId);
        }
        return false;
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
        int maxSpecializations = ConfigHandler.getMaxSpecializations();
        return getPlayerSpecializationCount(player) < maxSpecializations;
    }

    public void saveAllSpecializations(NBTTagCompound compound) {
        NBTTagList specializationList = new NBTTagList();
        for (Specialization spec : specializations.values()) {
            NBTTagCompound specCompound = new NBTTagCompound();
            spec.writeToNBT(specCompound);
            specializationList.appendTag(specCompound);
        }
        compound.setTag("AllSpecializations", specializationList);
    }

    public void loadAllSpecializations(NBTTagCompound compound) {
        specializations.clear();
        if (compound.hasKey("AllSpecializations", Constants.NBT.TAG_LIST)) {
            NBTTagList specializationList = compound.getTagList("AllSpecializations", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < specializationList.tagCount(); i++) {
                NBTTagCompound specCompound = specializationList.getCompoundTagAt(i);
                Specialization spec = new Specialization();
                spec.readFromNBT(specCompound);
                specializations.put(spec.getId(), spec);
            }
        }
    }

    public boolean hasSpecialization(String specializationId) {
        return specializations.containsKey(specializationId);
    }
}
