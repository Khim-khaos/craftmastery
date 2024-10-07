package com.example.craftmastery.crafting;

import java.util.*;

public class CraftingManager {
    private static CraftingManager instance;
    private Map<String, List<CraftRecipe>> recipes;

    private CraftingManager() {
        recipes = new LinkedHashMap<>();
    }

    public static CraftingManager getInstance() {
        if (instance == null) {
            instance = new CraftingManager();
        }
        return instance;
    }

    public void addCategory(String category) {
        if (!recipes.containsKey(category)) {
            recipes.put(category, new ArrayList<>());
        }
    }

    public void addRecipe(String category, CraftRecipe recipe) {
        recipes.computeIfAbsent(category, k -> new ArrayList<>()).add(recipe);
    }

    public List<CraftRecipe> getRecipes(String category) {
        return recipes.getOrDefault(category, new ArrayList<>());
    }

    public List<String> getCategories() {
        return new ArrayList<>(recipes.keySet());
    }
}
