package com.example.craftmastery.player;

import com.example.craftmastery.recipe.CustomRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

    public void unlockRecipe(CustomRecipe recipe, EntityPlayer player) {
        if (unlockedRecipes.add(recipe.getName())) {
            applyRecipeUnlockEffect(player);
        }
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

    public void addExperience(int exp, EntityPlayer player) {
        this.experience += exp;
        checkLevelUp(player);
    }

    private void checkLevelUp(EntityPlayer player) {
        int expNeeded = getExpNeededForNextLevel();
        while (experience >= expNeeded) {
            level++;
            experience -= expNeeded;
            expNeeded = getExpNeededForNextLevel();
            onLevelUp(player);
        }
    }

    private int getExpNeededForNextLevel() {
        return level * 100; // Простая формула, можно усложнить
    }

    private void onLevelUp(EntityPlayer player) {
        craftPoints += 5; // Награда за повышение уровня
        applyLevelUpEffect(player);
    }

    private void applyLevelUpEffect(EntityPlayer player) {
        // Пример эффекта: Скорость на 30 секунд
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 600, 1));
    }

    private void applyRecipeUnlockEffect(EntityPlayer player) {
        // Пример эффекта: Регенерация на 10 секунд
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 200, 0));
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
}
