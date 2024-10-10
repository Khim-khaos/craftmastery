package com.craftmastery.crafting;

import java.util.HashSet;
import java.util.Set;

public class CraftCategory {
    private String name;
    private Set<String> recipes;

    public CraftCategory(String name) {
        this.name = name;
        this.recipes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void addRecipe(String recipeId) {
        recipes.add(recipeId);
    }

    public void removeRecipe(String recipeId) {
        recipes.remove(recipeId);
    }

    public boolean hasRecipe(String recipeId) {
        return recipes.contains(recipeId);
    }

    public Set<String> getRecipes() {
        return new HashSet<>(recipes);
    }
}
