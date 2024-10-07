package com.example.craftmastery.crafting;

import net.minecraft.item.ItemStack;

public class CraftRecipe {
    private ItemStack output;
    private ItemStack[] ingredients;
    private int pointCost;
    private String type; // "basic", "technical", "magical"

    public CraftRecipe(ItemStack output, ItemStack[] ingredients, int pointCost, String type) {
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.type = type;
    }

    // Геттеры и сеттеры
}
