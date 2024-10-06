package com.example.craftmastery.points;

import com.example.craftmastery.recipe.CustomRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.Set;

public class PlayerPoints {
    private int craftPoints;
    private int resetPoints;
    private int cancelPoints;

    public PlayerPoints() {
        this.craftPoints = 0;
        this.resetPoints = 0;
        this.cancelPoints = 0;
    }

    public void addCraftPoints(int amount) {
        this.craftPoints += amount;
    }

    public void addResetPoints(int amount) {
        this.resetPoints += amount;
    }

    public void addCancelPoints(int amount) {
        this.cancelPoints += amount;
    }

    public boolean spendCraftPoints(int amount) {
        if (craftPoints >= amount) {
            craftPoints -= amount;
            return true;
        }
        return false;
    }

    public boolean spendResetPoints(int amount) {
        if (resetPoints >= amount) {
            resetPoints -= amount;
            return true;
        }
        return false;
    }
    private Set<String> unlockedRecipes = new HashSet<>();

    public boolean unlockRecipe(CustomRecipe recipe) {
        if (spendCraftPoints(recipe.getPointCost())) {
            unlockedRecipes.add(recipe.getName());
            return true;
        }
        return false;
    }

    public boolean isRecipeUnlocked(CustomRecipe recipe) {
        return unlockedRecipes.contains(recipe.getName());
    }
    public boolean spendCancelPoints(int amount) {
        if (cancelPoints >= amount) {
            cancelPoints -= amount;
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

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("craftPoints", craftPoints);
        compound.setInteger("resetPoints", resetPoints);
        compound.setInteger("cancelPoints", cancelPoints);
    }

    public void loadNBTData(NBTTagCompound compound) {
        craftPoints = compound.getInteger("craftPoints");
        resetPoints = compound.getInteger("resetPoints");
        cancelPoints = compound.getInteger("cancelPoints");
    }
}
