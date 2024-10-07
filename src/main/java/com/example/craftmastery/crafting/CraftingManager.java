package com.example.craftmastery.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class CraftingManager {
    private static CraftingManager instance;
    private Map<String, List<CraftRecipe>> recipesByCategory;
    private Map<ResourceLocation, CraftRecipe> recipesById;

    private CraftingManager() {
        recipesByCategory = new LinkedHashMap<>();
        recipesById = new HashMap<>();
    }

    public static CraftingManager getInstance() {
        if (instance == null) {
            instance = new CraftingManager();
        }
        return instance;
    }

    public void addCategory(String category) {
        if (!recipesByCategory.containsKey(category)) {
            recipesByCategory.put(category, new ArrayList<>());
        }
    }

    public Set<ResourceLocation> getAllRecipeIds() {
        return recipesById.keySet();
    }

    public void addRecipe(String category, CraftRecipe recipe) {
        recipesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(recipe);
        recipesById.put(recipe.getId(), recipe);
    }

    public List<CraftRecipe> getRecipes(String category) {
        return recipesByCategory.getOrDefault(category, new ArrayList<>());
    }

    public List<String> getCategories() {
        return new ArrayList<>(recipesByCategory.keySet());
    }

    public CraftRecipe getRecipeById(ResourceLocation id) {
        return recipesById.get(id);
    }

    public CraftRecipe findMatchingRecipe(List<ItemStack> craftingGrid) {
        for (CraftRecipe recipe : recipesById.values()) {
            if (recipe.matches(craftingGrid)) {
                return recipe;
            }
        }
        return null;
    }
}
