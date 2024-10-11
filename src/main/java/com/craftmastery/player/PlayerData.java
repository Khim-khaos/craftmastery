package com.craftmastery.player;

import com.craftmastery.config.ConfigHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerData {
    private int level;
    private int experience;
    private int learningPoints;
    private int specializationResetPoints;
    private Map<String, Integer> specializations;
    private Set<String> unlockedRecipes;

    public PlayerData() {
        this.level = 1;
        this.experience = 0;
        this.learningPoints = 0;
        this.specializationResetPoints = 0;
        this.specializations = new HashMap<>();
        this.unlockedRecipes = new HashSet<>();
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getLearningPoints() {
        return learningPoints;
    }

    public Map<String, Integer> getSpecializations() {
        return new HashMap<>(specializations);
    }

    public void addExperience(int amount) {
        this.experience += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expForNextLevel = getExpForNextLevel();
        while (this.experience >= expForNextLevel) {
            this.experience -= expForNextLevel;
            levelUp();
            expForNextLevel = getExpForNextLevel();
        }
    }

    private void levelUp() {
        this.level++;
        this.learningPoints += ConfigHandler.getBaseLearningPointsPerLevel();
    }

    public int getExpForNextLevel() {
        return this.level * 100;
    }

    public boolean useLearningPoints(int amount) {
        if (this.learningPoints >= amount) {
            this.learningPoints -= amount;
            return true;
        }
        return false;
    }

    public void addLearningPoints(int amount) {
        this.learningPoints += amount;
    }

    public int getSpecializationResetPoints() {
        return specializationResetPoints;
    }

    public void setSpecializationResetPoints(int points) {
        this.specializationResetPoints = points;
    }

    public void addSpecializationResetPoints(int points) {
        this.specializationResetPoints += points;
    }

    public boolean useSpecializationResetPoints(int points) {
        if (this.specializationResetPoints >= points) {
            this.specializationResetPoints -= points;
            return true;
        }
        return false;
    }

    public void addSpecializationPoints(String specializationId, int points) {
        specializations.put(specializationId, specializations.getOrDefault(specializationId, 0) + points);
    }

    public int getSpecializationPoints(String specializationId) {
        return specializations.getOrDefault(specializationId, 0);
    }

    public void unlockRecipe(String recipeId) {
        unlockedRecipes.add(recipeId);
    }

    public boolean hasUnlockedRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("Level", level);
        compound.setInteger("Experience", experience);
        compound.setInteger("LearningPoints", learningPoints);
        compound.setInteger("SpecializationResetPoints", specializationResetPoints);

        NBTTagCompound specializationsNBT = new NBTTagCompound();
        for (Map.Entry<String, Integer> entry : specializations.entrySet()) {
            specializationsNBT.setInteger(entry.getKey(), entry.getValue());
        }
        compound.setTag("Specializations", specializationsNBT);

        NBTTagList recipeList = new NBTTagList();
        for (String recipe : unlockedRecipes) {
            recipeList.appendTag(new NBTTagString(recipe));
        }
        compound.setTag("UnlockedRecipes", recipeList);
    }

    public void loadNBTData(NBTTagCompound compound) {
        level = compound.getInteger("Level");
        experience = compound.getInteger("Experience");
        learningPoints = compound.getInteger("LearningPoints");
        specializationResetPoints = compound.getInteger("SpecializationResetPoints");

        specializations.clear();
        NBTTagCompound specializationsNBT = compound.getCompoundTag("Specializations");
        for (String key : specializationsNBT.getKeySet()) {
            specializations.put(key, specializationsNBT.getInteger(key));
        }

        unlockedRecipes.clear();
        NBTTagList recipeList = compound.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            unlockedRecipes.add(recipeList.getStringTagAt(i));
        }
    }
}
