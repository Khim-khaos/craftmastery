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
    private int resetPoints;
    private int cancelPoints;

    public PlayerProgress() {
        this.unlockedRecipes = new HashSet<>();
        this.craftPoints = 0;
        this.resetPoints = 0;
        this.cancelPoints = 0;
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

    public void addResetPoints(int points) {
        this.resetPoints += points;
    }

    public void addCancelPoints(int points) {
        this.cancelPoints += points;
    }

    public boolean spendCraftPoints(int points) {
        if (craftPoints >= points) {
            craftPoints -= points;
            return true;
        }
        return false;
    }

    public boolean spendResetPoints(int points) {
        if (resetPoints >= points) {
            resetPoints -= points;
            return true;
        }
        return false;
    }

    public boolean spendCancelPoints(int points) {
        if (cancelPoints >= points) {
            cancelPoints -= points;
            return true;
        }
        return false;
    }

    public int getCraftPoints() {
        return craftPoints;
    }

    public int getResetPoints() {
        return resetPoints;
    }

    public int getCancelPoints() {
        return cancelPoints;
    }

    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public void resetProgress() {
        unlockedRecipes.clear();
        craftPoints = 0;
        resetPoints = 0;
        cancelPoints = 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagList recipeList = new NBTTagList();
        for (String recipe : unlockedRecipes) {
            recipeList.appendTag(new NBTTagString(recipe));
        }
        nbt.setTag("UnlockedRecipes", recipeList);

        nbt.setInteger("CraftPoints", craftPoints);
        nbt.setInteger("ResetPoints", resetPoints);
        nbt.setInteger("CancelPoints", cancelPoints);

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        unlockedRecipes.clear();
        NBTTagList recipeList = nbt.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            unlockedRecipes.add(recipeList.getStringTagAt(i));
        }

        craftPoints = nbt.getInteger("CraftPoints");
        resetPoints = nbt.getInteger("ResetPoints");
        cancelPoints = nbt.getInteger("CancelPoints");
    }
}
