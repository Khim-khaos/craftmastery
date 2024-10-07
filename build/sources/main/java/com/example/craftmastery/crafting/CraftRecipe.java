package com.example.craftmastery.crafting;

import com.example.craftmastery.player.PlayerData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
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
    private List<CraftRecipe> dependencies;
    private static final Set<Item> ALLOWED_ITEMS = new HashSet<>();

    public CraftRecipe(ResourceLocation id, ItemStack output, ItemStack[] ingredients, int pointCost, String type) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.type = type;
        this.unlocked = false;
        this.dependencies = new ArrayList<>();
    }

    public ResourceLocation getId() {
        return id;
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

    public boolean canBeUnlocked(PlayerData playerData) {
        for (CraftRecipe dependency : dependencies) {
            if (!playerData.isRecipeUnlocked(dependency)) {
                return false;
            }
        }
        return true;
    }
    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public List<CraftRecipe> getDependencies() {
        return dependencies;
    }

    public void addDependency(CraftRecipe recipe) {
        dependencies.add(recipe);
    }

    public static void addAllowedItem(Item item) {
        ALLOWED_ITEMS.add(item);
    }

    public static boolean isItemAllowed(ItemStack itemStack) {
        return ALLOWED_ITEMS.contains(itemStack.getItem());
    }

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
