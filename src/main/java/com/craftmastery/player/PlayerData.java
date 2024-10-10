package com.craftmastery.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private int level;
    private int experience;
    private int learningPoints;
    private Set<String> unlockedRecipes;

    public PlayerData() {
        this.level = 1;
        this.experience = 0;
        this.learningPoints = 0;
        this.unlockedRecipes = new HashSet<>();
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
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
        this.learningPoints += 1; // You can adjust this value
    }

    public int getExpForNextLevel() {
        return this.level * 100; // Simple linear progression
    }

    public int getLearningPoints() {
        return learningPoints;
    }

    public boolean useLearningPoints(int amount) {
        if (this.learningPoints >= amount) {
            this.learningPoints -= amount;
            return true;
        }
        return false;
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

        unlockedRecipes.clear();
        NBTTagList recipeList = compound.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            unlockedRecipes.add(recipeList.getStringTagAt(i));
        }
    }
}
