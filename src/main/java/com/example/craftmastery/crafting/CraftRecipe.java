package com.example.craftmastery.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CraftRecipe {
    private ItemStack output;
    private ItemStack[] ingredients;
    private int pointCost;
    private String type; // "basic", "technical", "magical"
    private boolean unlocked;
    private static final Set<Item> ALLOWED_ITEMS = new HashSet<>();

    public CraftRecipe(ItemStack output, ItemStack[] ingredients, int pointCost, String type) {
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.type = type;
        this.unlocked = false;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack[] getIngredients() {
        return ingredients;
    }

    public int getPointCost() {
        return pointCost;
    }

    public String getType() {
        return type;
    }

    public static void addAllowedItem(Item item) {
        ALLOWED_ITEMS.add(item);
    }

    public static boolean isItemAllowed(Item item) {
        return ALLOWED_ITEMS.contains(item);
    }
}
