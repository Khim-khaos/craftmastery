package com.example.craftmastery.recipe;

import net.minecraft.item.ItemStack;
import java.util.List;

public class CustomRecipe {
    private String name;
    private ItemStack output;
    private List<ItemStack> ingredients;
    private int pointCost;
    private int pointReward;
    private String category;
    private int difficultyLevel;

    public CustomRecipe(String name, ItemStack output, List<ItemStack> ingredients, int pointCost, int pointReward, String category, int difficultyLevel) {
        this.name = name;
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.pointReward = pointReward;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
    }

    public String getName() {
        return name;
    }

    public ItemStack getOutput() {
        return output;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public int getPointCost() {
        return pointCost;
    }

    public int getPointReward() {
        return pointReward;
    }

    public String getCategory() {
        return category;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
}
