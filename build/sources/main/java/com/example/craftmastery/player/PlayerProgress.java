package com.example.craftmastery.player;

import com.example.craftmastery.recipe.CustomRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PlayerProgress {
    private final Set<String> unlockedRecipes;
    private int craftPoints;
    private int level;
    private int experience;

    public PlayerProgress() {
        this.unlockedRecipes = new HashSet<>();
        this.craftPoints = 0;
        this.level = 1;
        this.experience = 0;
    }

    public boolean isRecipeUnlocked(CustomRecipe recipe) {
        return unlockedRecipes.contains(recipe.getName());
    }

    public void unlockRecipe(CustomRecipe recipe) {
        unlockedRecipes.add(recipe.getName());
    }

    public void addCraftPoints(int points) {
        this.craftPoints += points;
    }

    public boolean spendCraftPoints(int points) {
        if (craftPoints >= points) {
            craftPoints -= points;
            return true;
        }
        return false;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expNeeded = getExpNeededForNextLevel();
        while (experience >= expNeeded) {
            level++;
            experience -= expNeeded;
            expNeeded = getExpNeededForNextLevel();
            onLevelUp();
        }
    }

    public int getExpNeededForNextLevel() {
        return level * 100; // Simple formula, can be adjusted
    }

    private void onLevelUp() {
        craftPoints += 5; // Reward for leveling up
    }

    public int getCraftPoints() {
        return craftPoints;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList recipeList = new NBTTagList();
        for (String recipe : unlockedRecipes) {
            recipeList.appendTag(new NBTTagString(recipe));
        }
        nbt.setTag("UnlockedRecipes", recipeList);
        nbt.setInteger("CraftPoints", craftPoints);
        nbt.setInteger("Level", level);
        nbt.setInteger("Experience", experience);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        unlockedRecipes.clear();
        NBTTagList recipeList = nbt.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            unlockedRecipes.add(recipeList.getStringTagAt(i));
        }
        craftPoints = nbt.getInteger("CraftPoints");
        level = nbt.getInteger("Level");
        experience = nbt.getInteger("Experience");
    }

    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public void setUnlockedRecipes(Set<String> recipes) {
        this.unlockedRecipes.clear();
        this.unlockedRecipes.addAll(recipes);
    }

    public void setCraftPoints(int points) {
        this.craftPoints = points;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
        checkLevelUp();
    }
}
