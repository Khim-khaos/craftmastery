package com.craftmastery.crafting;

import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.player.PlayerData;
import net.minecraft.entity.player.EntityPlayer;

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
        int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);

        if (playerData.getLearningPoints() >= cost) {
            if (playerData.useLearningPoints(cost)) {
                CraftManager.getInstance().unlockRecipeForPlayer(player, recipeId);
                return true;
            }
        }
        return false;
    }

    public boolean resetRecipe(EntityPlayer player, String recipeId) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        int resetCost = 1; // You might want to make this configurable

        if (playerData.useCraftResetPoints(resetCost)) {
            CraftManager.getInstance().lockRecipeForPlayer(player, recipeId);
            int refundPoints = CraftManager.getInstance().getRecipeLearningCost(recipeId) / 2; // Refund half the cost
            playerData.addLearningPoints(refundPoints);
            return true;
        }
        return false;
    }

    public void grantCraftResetPoints(EntityPlayer player, int amount) {
        PlayerDataManager.getInstance().getPlayerData(player).addCraftResetPoints(amount);
    }
}
