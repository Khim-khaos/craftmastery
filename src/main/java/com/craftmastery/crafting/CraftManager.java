package com.craftmastery.crafting;

import com.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

public class CraftManager {
    private static CraftManager instance;
    private Map<String, Integer> recipeLearningCosts;
    private Map<String, List<IRecipe>> recipesByType;

    private CraftManager() {
        recipeLearningCosts = new HashMap<>();
        recipesByType = new HashMap<>();
    }

    public static CraftManager getInstance() {
        if (instance == null) {
            instance = new CraftManager();
        }
        return instance;
    }

    public void initializeRecipes() {
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            String recipeId = recipe.getRegistryName().toString();
            String recipeType = getRecipeType(recipe);
            recipesByType.computeIfAbsent(recipeType, k -> new ArrayList<>()).add(recipe);
            recipeLearningCosts.put(recipeId, 1); // Default cost
        }
    }

    private String getRecipeType(IRecipe recipe) {
        // Implement logic to determine recipe type
        // This is a placeholder implementation
        return "normal";
    }

    public boolean isRecipeUnlocked(EntityPlayer player, String recipeId) {
        return PlayerDataManager.getInstance().getPlayerData(player).hasUnlockedRecipe(recipeId);
    }

    public void unlockRecipeForPlayer(EntityPlayer player, String recipeId) {
        PlayerDataManager.getInstance().getPlayerData(player).unlockRecipe(recipeId);
    }

    public void lockRecipeForPlayer(EntityPlayer player, String recipeId) {
        PlayerDataManager.getInstance().getPlayerData(player).getUnlockedRecipes().remove(recipeId);
    }

    public boolean canPlayerLearnRecipe(EntityPlayer player, String recipeId) {
        if (isRecipeUnlocked(player, recipeId)) {
            return false; // Player already knows this recipe
        }

        int cost = getRecipeLearningCost(recipeId);
        return PlayerDataManager.getInstance().getPlayerData(player).getLearningPoints() >= cost;
    }

    public boolean learnRecipe(EntityPlayer player, String recipeId) {
        if (canPlayerLearnRecipe(player, recipeId)) {
            int cost = getRecipeLearningCost(recipeId);
            if (PlayerDataManager.getInstance().getPlayerData(player).useLearningPoints(cost)) {
                unlockRecipeForPlayer(player, recipeId);
                return true;
            }
        }
        return false;
    }

    public int getRecipeLearningCost(String recipeId) {
        return recipeLearningCosts.getOrDefault(recipeId, 1);
    }

    public void setRecipeLearningCost(String recipeId, int cost) {
        recipeLearningCosts.put(recipeId, cost);
    }

    public List<IRecipe> getRecipesByType(String type) {
        return recipesByType.getOrDefault(type, new ArrayList<>());
    }

    public IRecipe getRecipeById(String recipeId) {
        return ForgeRegistries.RECIPES.getValue(new ResourceLocation(recipeId));
    }

    public List<String> getUnlockedRecipesForPlayer(EntityPlayer player) {
        return new ArrayList<>(PlayerDataManager.getInstance().getPlayerData(player).getUnlockedRecipes());
    }
}
