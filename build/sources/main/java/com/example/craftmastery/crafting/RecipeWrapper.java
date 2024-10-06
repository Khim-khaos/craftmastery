package com.example.craftmastery.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RecipeWrapper {
    private final String id;
    private final IRecipe recipe;
    private final int pointCost;
    private final String categoryId;
    private final int requiredLevel;
    private final List<String> prerequisites;
    private final double experienceReward;

    public RecipeWrapper(IRecipe recipe, int pointCost, int requiredLevel, double experienceReward) {
        this(recipe.getRegistryName().toString(), recipe, pointCost, "minecraft", requiredLevel, experienceReward);
    }

    public RecipeWrapper(String id, IRecipe recipe, int pointCost, String categoryId, int requiredLevel, double experienceReward) {
        this.id = id;
        this.recipe = recipe;
        this.pointCost = pointCost;
        this.categoryId = categoryId;
        this.requiredLevel = requiredLevel;
        this.prerequisites = new ArrayList<>();
        this.experienceReward = experienceReward;
    }

    public String getId() {
        return id;
    }

    public IRecipe getRecipe() {
        return recipe;
    }

    public int getPointCost() {
        return pointCost;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void addPrerequisite(String recipeId) {
        prerequisites.add(recipeId);
    }

    public double getExperienceReward() {
        return experienceReward;
    }

    public ItemStack getOutput() {
        return recipe.getRecipeOutput();
    }

    public ResourceLocation getRegistryName() {
        return recipe.getRegistryName();
    }

    @Override
    public String toString() {
        return "RecipeWrapper{" +
                "id='" + id + '\'' +
                ", recipe=" + recipe +
                ", pointCost=" + pointCost +
                ", categoryId='" + categoryId + '\'' +
                ", requiredLevel=" + requiredLevel +
                ", prerequisites=" + prerequisites +
                ", experienceReward=" + experienceReward +
                '}';
    }
}
