package com.craftmastery.specialization;

import java.util.HashSet;
import java.util.Set;

public class Specialization {
    private String id;
    private String name;
    private String description;
    private Set<String> recipes;

    public Specialization(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recipes = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
