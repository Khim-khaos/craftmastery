package com.example.craftmastery.player;

import com.example.craftmastery.crafting.CraftRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class PlayerData implements INBTSerializable<NBTTagCompound> {
    private int points;
    private Set<ResourceLocation> unlockedRecipes;
    private int totalPointsEarned;
    private int totalRecipesUnlocked;
    private int resetPoints;
    private int undoPoints;

    public PlayerData() {
        this.points = 0;
        this.unlockedRecipes = new HashSet<>();
        this.totalPointsEarned = 0;
        this.totalRecipesUnlocked = 0;
        this.resetPoints = 0;
        this.undoPoints = 0;
    }

    public void addPoints(int amount) {
        this.points += amount;
        this.totalPointsEarned += amount;
    }

    public boolean spendPoints(int amount) {
        if (this.points >= amount) {
            this.points -= amount;
            return true;
        }
        return false;
    }

    public boolean isRecipeUnlocked(ResourceLocation recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public boolean unlockRecipe(CraftRecipe recipe) {
        if (points >= recipe.getPointCost() && recipe.canBeUnlocked(this)) {
            if (spendPoints(recipe.getPointCost())) {
                unlockedRecipes.add(recipe.getId());
                totalRecipesUnlocked++;
                return true;
            }
        }
        return false;
    }

    public Set<ResourceLocation> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public int getPoints() {
        return points;
    }

    public int getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public int getTotalRecipesUnlocked() {
        return totalRecipesUnlocked;
    }

    public void addResetPoints(int amount) {
        this.resetPoints += amount;
    }

    public void addUndoPoints(int amount) {
        this.undoPoints += amount;
    }

    public int getResetPoints() {
        return resetPoints;
    }

    public int getUndoPoints() {
        return undoPoints;
    }

    public void resetProgress() {
        points = 0;
        unlockedRecipes.clear();
        totalRecipesUnlocked = 0;
        // Не сбрасываем totalPointsEarned, чтобы сохранить историю
    }

    public void lockAllRecipes() {
        unlockedRecipes.clear();
        totalRecipesUnlocked = 0;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("points", points);
        nbt.setInteger("totalPointsEarned", totalPointsEarned);
        nbt.setInteger("totalRecipesUnlocked", totalRecipesUnlocked);
        nbt.setInteger("resetPoints", resetPoints);
        nbt.setInteger("undoPoints", undoPoints);

        NBTTagList unlockedList = new NBTTagList();
        for (ResourceLocation recipeId : unlockedRecipes) {
            NBTTagCompound recipeTag = new NBTTagCompound();
            recipeTag.setString("id", recipeId.toString());
            unlockedList.appendTag(recipeTag);
        }
        nbt.setTag("unlockedRecipes", unlockedList);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        points = nbt.getInteger("points");
        totalPointsEarned = nbt.getInteger("totalPointsEarned");
        totalRecipesUnlocked = nbt.getInteger("totalRecipesUnlocked");
        resetPoints = nbt.getInteger("resetPoints");
        undoPoints = nbt.getInteger("undoPoints");

        unlockedRecipes.clear();
        NBTTagList unlockedList = nbt.getTagList("unlockedRecipes", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < unlockedList.tagCount(); i++) {
            NBTTagCompound recipeTag = unlockedList.getCompoundTagAt(i);
            unlockedRecipes.add(new ResourceLocation(recipeTag.getString("id")));
        }
    }

    public void syncWithClient() {
        // Здесь будет код для синхронизации данных с клиентом
        // Это будет реализовано позже, когда мы настроим сетевой код
    }
}
