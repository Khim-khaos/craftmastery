package com.example.craftmastery.progression;

import com.example.craftmastery.crafting.RecipeWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PlayerProgression {
    private final PlayerLevel playerLevel;
    private int craftPoints;
    private int resetPoints;
    private final Set<String> unlockedRecipes;
    private final Set<String> unlockedUpgrades;

    public PlayerProgression() {
        this.playerLevel = new PlayerLevel();
        this.craftPoints = 0;
        this.resetPoints = 0;
        this.unlockedRecipes = new HashSet<>();
        this.unlockedUpgrades = new HashSet<>();
    }

    public int getLevel() {
        return playerLevel.getLevel();
    }

    public int getExperience() {
        return playerLevel.getExperience();
    }

    public int getExperienceToNextLevel() {
        return playerLevel.getExperienceToNextLevel();
    }

    public void addExperience(int amount) {
        playerLevel.addExperience(amount);
    }

    public void setLevel(int level) {
        playerLevel.setLevel(level);
    }

    public int getCraftPoints() {
        return craftPoints;
    }

    public void addCraftPoints(int amount) {
        craftPoints += amount;
    }

    public boolean spendCraftPoints(int amount) {
        if (craftPoints >= amount) {
            craftPoints -= amount;
            return true;
        }
        return false;
    }

    public int getResetPoints() {
        return resetPoints;
    }

    public void addResetPoints(int amount) {
        resetPoints += amount;
    }

    public boolean spendResetPoints(int amount) {
        if (resetPoints >= amount) {
            resetPoints -= amount;
            return true;
        }
        return false;
    }

    public boolean isRecipeUnlocked(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public boolean unlockRecipe(RecipeWrapper recipe) {
        if (canUnlockRecipe(recipe)) {
            unlockedRecipes.add(recipe.getId());
            spendCraftPoints(recipe.getPointCost());
            return true;
        }
        return false;
    }

    public boolean canUnlockRecipe(RecipeWrapper recipe) {
        return getLevel() >= recipe.getRequiredLevel() &&
                getCraftPoints() >= recipe.getPointCost() &&
                (recipe.getPrerequisites().isEmpty() ||
                        recipe.getPrerequisites().stream().allMatch(this::isRecipeUnlocked));
    }

    public boolean isUpgradeUnlocked(String upgradeId) {
        return unlockedUpgrades.contains(upgradeId);
    }

    public boolean unlockUpgrade(String upgradeId, int cost) {
        if (spendCraftPoints(cost)) {
            unlockedUpgrades.add(upgradeId);
            return true;
        }
        return false;
    }

    public void resetProgress() {
        playerLevel.reset();
        craftPoints = 0;
        unlockedRecipes.clear();
        unlockedUpgrades.clear();
        // Возможно, здесь нужно добавить логику для начисления resetPoints
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound levelNBT = new NBTTagCompound();
        playerLevel.writeToNBT(levelNBT);
        nbt.setTag("PlayerLevel", levelNBT);

        nbt.setInteger("CraftPoints", craftPoints);
        nbt.setInteger("ResetPoints", resetPoints);

        NBTTagList unlockedRecipesNBT = new NBTTagList();
        for (String recipeId : unlockedRecipes) {
            unlockedRecipesNBT.appendTag(new NBTTagString(recipeId));
        }
        nbt.setTag("UnlockedRecipes", unlockedRecipesNBT);

        NBTTagList unlockedUpgradesNBT = new NBTTagList();
        for (String upgradeId : unlockedUpgrades) {
            unlockedUpgradesNBT.appendTag(new NBTTagString(upgradeId));
        }
        nbt.setTag("UnlockedUpgrades", unlockedUpgradesNBT);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("PlayerLevel", Constants.NBT.TAG_COMPOUND)) {
            playerLevel.readFromNBT(nbt.getCompoundTag("PlayerLevel"));
        }

        craftPoints = nbt.getInteger("CraftPoints");
        resetPoints = nbt.getInteger("ResetPoints");

        unlockedRecipes.clear();
        NBTTagList unlockedRecipesNBT = nbt.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < unlockedRecipesNBT.tagCount(); i++) {
            unlockedRecipes.add(unlockedRecipesNBT.getStringTagAt(i));
        }

        unlockedUpgrades.clear();
        NBTTagList unlockedUpgradesNBT = nbt.getTagList("UnlockedUpgrades", Constants.NBT.TAG_STRING);
        for (int i = 0; i < unlockedUpgradesNBT.tagCount(); i++) {
            unlockedUpgrades.add(unlockedUpgradesNBT.getStringTagAt(i));
        }
    }
}
