package com.craftmastery.player;

import com.craftmastery.specialization.Specialization;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class PlayerData {
    private int level;
    private int experience;
    private int learningPoints;
    private int craftResetPoints;
    private Map<String, Integer> specializationLevels;
    private Set<String> unlockedRecipes;

    public PlayerData() {
        this.level = 1;
        this.experience = 0;
        this.learningPoints = 0;
        this.craftResetPoints = 0;
        this.specializationLevels = new HashMap<>();
        this.unlockedRecipes = new HashSet<>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int amount) {
        this.experience += amount;
    }

    public int getLearningPoints() {
        return learningPoints;
    }

    public void setLearningPoints(int learningPoints) {
        this.learningPoints = learningPoints;
    }

    public void addLearningPoints(int amount) {
        this.learningPoints += amount;
    }

    public boolean useLearningPoints(int amount) {
        if (this.learningPoints >= amount) {
            this.learningPoints -= amount;
            return true;
        }
        return false;
    }

    public int getCraftResetPoints() {
        return craftResetPoints;
    }

    public boolean useCraftResetPoints(int points) {
        if (craftResetPoints >= points) {
            craftResetPoints -= points;
            return true;
        }
        return false;
    }

    public void addCraftResetPoints(int points) {
        craftResetPoints += points;
    }

    public Map<String, Integer> getSpecializationLevels() {
        return specializationLevels;
    }

    public int getSpecializationLevel(String specializationId) {
        return specializationLevels.getOrDefault(specializationId, 0);
    }

    public void setSpecializationLevel(String specializationId, int level) {
        specializationLevels.put(specializationId, level);
    }

    public void incrementSpecializationLevel(String specializationId) {
        specializationLevels.put(specializationId, getSpecializationLevel(specializationId) + 1);
    }

    public Set<String> getUnlockedRecipes() {
        return unlockedRecipes;
    }

    public boolean hasUnlockedRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public void unlockRecipe(String recipeId) {
        unlockedRecipes.add(recipeId);
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("Level", level);
        compound.setInteger("Experience", experience);
        compound.setInteger("LearningPoints", learningPoints);
        compound.setInteger("CraftResetPoints", craftResetPoints);

        NBTTagCompound specializationsNBT = new NBTTagCompound();
        for (Map.Entry<String, Integer> entry : specializationLevels.entrySet()) {
            specializationsNBT.setInteger(entry.getKey(), entry.getValue());
        }
        compound.setTag("Specializations", specializationsNBT);

        NBTTagList recipesList = new NBTTagList();
        for (String recipeId : unlockedRecipes) {
            recipesList.appendTag(new NBTTagString(recipeId));
        }
        compound.setTag("UnlockedRecipes", recipesList);
    }

    public void loadNBTData(NBTTagCompound compound) {
        level = compound.getInteger("Level");
        experience = compound.getInteger("Experience");
        learningPoints = compound.getInteger("LearningPoints");
        craftResetPoints = compound.getInteger("CraftResetPoints");

        specializationLevels.clear();
        NBTTagCompound specializationsNBT = compound.getCompoundTag("Specializations");
        for (String key : specializationsNBT.getKeySet()) {
            specializationLevels.put(key, specializationsNBT.getInteger(key));
        }

        unlockedRecipes.clear();
        NBTTagList recipesList = compound.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipesList.tagCount(); i++) {
            unlockedRecipes.add(recipesList.getStringTagAt(i));
        }
    }
}
