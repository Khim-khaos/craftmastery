package com.example.craftmastery.recipe;

import net.minecraft.item.ItemStack;
import java.util.List;

public class CustomRecipe {
    private final String name;
    private final ItemStack output;
    private final List<ItemStack> ingredients;
    private final int pointCost;
    private final int pointReward;
    private final int expReward;
    private final RecipeCategory category;
    private final int difficultyLevel;

    public CustomRecipe(String name, ItemStack output, List<ItemStack> ingredients, int pointCost, int pointReward, int expReward, RecipeCategory category, int difficultyLevel) {
        this.name = name;
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.pointReward = pointReward;
        this.expReward = expReward;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
    }

    // Getters
    public String getName() { return name; }
    public ItemStack getOutput() { return output; }
    public List<ItemStack> getIngredients() { return ingredients; }
    public int getPointCost() { return pointCost; }
    public int getPointReward() { return pointReward; }
    public int getExpReward() { return expReward; }
    public RecipeCategory getCategory() { return category; }
    public int getDifficultyLevel() { return difficultyLevel; }
}
