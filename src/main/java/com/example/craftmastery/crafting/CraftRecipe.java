package com.example.craftmastery.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftRecipe {
    private ResourceLocation id;
    private ItemStack output;
    private ItemStack[] ingredients;
    private int pointCost;
    private String type; // "basic", "technical", "magical"
    private boolean unlocked;
    private static final Set<Item> ALLOWED_ITEMS = new HashSet<>();

    public CraftRecipe(ResourceLocation id, ItemStack output, ItemStack[] ingredients, int pointCost, String type) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.type = type;
        this.unlocked = false;
    }

    public ResourceLocation getId() {
        return id;
    }

    // ... существующие методы ...

    public boolean matches(List<ItemStack> craftingGrid) {
        if (craftingGrid.size() != ingredients.length) {
            return false;
        }

        for (int i = 0; i < ingredients.length; i++) {
            ItemStack ingredient = ingredients[i];
            ItemStack gridItem = craftingGrid.get(i);

            if (ingredient == null && !gridItem.isEmpty()) {
                return false;
            }

            if (ingredient != null && (gridItem.isEmpty() || !ItemStack.areItemsEqual(ingredient, gridItem))) {
                return false;
            }
        }

        return true;
    }
}
