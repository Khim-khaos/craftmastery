package com.example.craftmastery.player;

import com.example.craftmastery.crafting.CraftRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private int points;
    private int resetPoints;
    private int undoPoints;
    private Set<String> unlockedRecipes;

    public PlayerData() {
        this.unlockedRecipes = new HashSet<>();
    }

    public void lockAllRecipes() {
        unlockedRecipes.clear();
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void addResetPoints(int amount) {
        this.resetPoints += amount;
    }

    public void addUndoPoints(int amount) {
        this.undoPoints += amount;
    }

    public boolean isRecipeUnlocked(CraftRecipe recipe) {
        return unlockedRecipes.contains(recipe.getOutput().getItem().getRegistryName().toString());
    }

    public boolean unlockRecipe(CraftRecipe recipe) {
        if (points >= recipe.getPointCost() && recipe.canBeUnlocked(this)) {
            points -= recipe.getPointCost();
            unlockedRecipes.add(recipe.getOutput().getItem().getRegistryName().toString());
            return true;
        }
        return false;
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("points", points);
        compound.setInteger("resetPoints", resetPoints);
        compound.setInteger("undoPoints", undoPoints);
        NBTTagList unlockedList = new NBTTagList();
        for (String recipe : unlockedRecipes) {
            unlockedList.appendTag(new NBTTagString(recipe));
        }
        compound.setTag("UnlockedRecipes", unlockedList);
    }

    public void loadNBTData(NBTTagCompound compound) {
        points = compound.getInteger("points");
        resetPoints = compound.getInteger("resetPoints");
        undoPoints = compound.getInteger("undoPoints");
        unlockedRecipes.clear();
        NBTTagList unlockedList = compound.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < unlockedList.tagCount(); i++) {
            unlockedRecipes.add(unlockedList.getStringTagAt(i));
        }
    }

    public int getPoints() {
        return points;
    }

    public int getResetPoints() {
        return resetPoints;
    }

    public int getUndoPoints() {
        return undoPoints;
    }
}
