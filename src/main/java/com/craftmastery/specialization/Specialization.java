package com.craftmastery.specialization;

import java.util.HashSet;
import java.util.Set;

public class Specialization {
    private String id;
    private String name;
    private Set<String> recipes;

    public Specialization(String id, String name) {
        this.id = id;
        this.name = name;
        this.recipes = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addRecipe(String recipeId) {
        recipes.add(recipeId);
    }

    public boolean hasRecipe(String recipeId) {
        return recipes.contains(recipeId);
    }

    public Set<String> getRecipes() {
        return new HashSet<>(recipes);
    }
}
