package com.example.craftmastery.recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategory {
    private String name;
    private List<CustomRecipe> recipes;

    public RecipeCategory(String name) {
        this.name = name;
        this.recipes = new ArrayList<>();
    }

    public void addRecipe(CustomRecipe recipe) {
        recipes.add(recipe);
    }

    public String getName() {
        return name;
    }
    public void removeRecipe(CustomRecipe recipe) {
        recipes.remove(recipe);
    }
    public List<CustomRecipe> getRecipes() {
        return new ArrayList<>(recipes);
    }
}
