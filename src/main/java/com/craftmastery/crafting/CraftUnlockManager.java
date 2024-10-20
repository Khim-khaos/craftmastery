package com.craftmastery.crafting;

import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class CraftUnlockManager {
    private static CraftUnlockManager instance;

    private CraftUnlockManager() {}

    public static CraftUnlockManager getInstance() {
        if (instance == null) {
            instance = new CraftUnlockManager();
        }
        return instance;
    }

    public boolean unlockRecipe(EntityPlayer player, String recipeId) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        if (playerData.hasUnlockedRecipe(recipeId)) {
            return false; // Recipe already unlocked
        }

        int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);
        if (playerData.useLearningPoints(cost)) {
            playerData.unlockRecipe(recipeId);
            return true;
        }
        return false;
    }

    public boolean resetCrafts(EntityPlayer player) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        if (playerData.useCraftResetPoints(1)) {
            // Reset all unlocked crafts
            playerData.getUnlockedRecipes().clear();
            // Return some learning points
            playerData.addLearningPoints(calculateReturnedLearningPoints(playerData));
            return true;
        }
        return false;
    }

    public void addCraftResetPoints(EntityPlayer player, int points) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        playerData.addCraftResetPoints(points);
    }

    private int calculateReturnedLearningPoints(PlayerData playerData) {
        // Implement logic to calculate returned learning points
        // For example, return 50% of spent points
        return playerData.getUnlockedRecipes().size() / 2;
    }

    public List<IRecipe> getUnlockedRecipes(EntityPlayer player) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        return CraftManager.getInstance().getRecipesByIds(playerData.getUnlockedRecipes());
    }

    public boolean canUnlockRecipe(EntityPlayer player, String recipeId) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);
        return playerData.getLearningPoints() >= cost;
    }
}
