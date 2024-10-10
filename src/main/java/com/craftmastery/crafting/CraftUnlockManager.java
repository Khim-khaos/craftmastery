package com.craftmastery.crafting;

import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class CraftUnlockManager {
    private static CraftUnlockManager instance;
    private Map<String, Integer> craftUnlockCosts;

    private CraftUnlockManager() {
        craftUnlockCosts = new HashMap<>();
    }

    public static CraftUnlockManager getInstance() {
        if (instance == null) {
            instance = new CraftUnlockManager();
        }
        return instance;
    }

    public void setCraftUnlockCost(String recipeId, int cost) {
        craftUnlockCosts.put(recipeId, cost);
    }

    public boolean canUnlockCraft(EntityPlayer player, String recipeId) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        int cost = craftUnlockCosts.getOrDefault(recipeId, 1);
        return playerData.getLearningPoints() >= cost;
    }

    public boolean unlockCraft(EntityPlayer player, String recipeId) {
        if (canUnlockCraft(player, recipeId)) {
            PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
            int cost = craftUnlockCosts.getOrDefault(recipeId, 1);
            if (playerData.useLearningPoints(cost)) {
                CraftManager.getInstance().unlockRecipeForPlayer(player, recipeId);
                return true;
            }
        }
        return false;
    }

    public boolean resetCraft(EntityPlayer player, String recipeId) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);
        if (playerData.useCraftResetPoints(1)) {
            CraftManager.getInstance().lockRecipeForPlayer(player, recipeId);
            return true;
        }
        return false;
    }
}
