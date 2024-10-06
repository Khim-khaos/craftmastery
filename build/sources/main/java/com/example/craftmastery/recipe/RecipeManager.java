package com.example.craftmastery.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager {
    private static final Map<String, RecipeCategory> categories = new HashMap<>();
    private static final List<CustomRecipe> allRecipes = new ArrayList<>();
    private static final Map<String, IRecipe> lockedVanillaRecipes = new HashMap<>();

    // Методы для работы с категориями
    public static void addCategory(RecipeCategory category) {
        categories.put(category.getName(), category);
    }

    public static List<RecipeCategory> getCategories() {
        return new ArrayList<>(categories.values());
    }
    public static CustomRecipe getRecipeByOutput(ItemStack output) {
        return allRecipes.stream()
                .filter(recipe -> ItemStack.areItemStacksEqual(recipe.getOutput(), output))
                .findFirst()
                .orElse(null);
    }
    public static RecipeCategory getCategory(String name) {
        return categories.get(name);
    }

    // Методы для работы с пользовательскими рецептами
    public static void addRecipe(CustomRecipe recipe) {
        allRecipes.add(recipe);
        RecipeCategory category = categories.get(recipe.getCategory());
        if (category == null) {
            category = new RecipeCategory(recipe.getCategory());
            addCategory(category);
        }
        category.addRecipe(recipe);
    }

    public static List<CustomRecipe> getAllRecipes() {
        return new ArrayList<>(allRecipes);
    }

    public static void removeRecipe(CustomRecipe recipe) {
        allRecipes.remove(recipe);
        RecipeCategory category = categories.get(recipe.getCategory());
        if (category != null) {
            category.removeRecipe(recipe);
        }
    }

    public static CustomRecipe getRecipeByName(String name) {
        return allRecipes.stream()
                .filter(recipe -> recipe.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    // Методы для работы с заблокированными ванильными рецептами
    public static void lockVanillaRecipe(IRecipe recipe) {
        String recipeName = recipe.getRegistryName().toString();
        lockedVanillaRecipes.put(recipeName, recipe);
    }

    public static void unlockVanillaRecipe(String recipeName) {
        lockedVanillaRecipes.remove(recipeName);
    }

    public static boolean isVanillaRecipeLocked(String recipeName) {
        return lockedVanillaRecipes.containsKey(recipeName);
    }

    public static Map<String, IRecipe> getLockedVanillaRecipes() {
        return new HashMap<>(lockedVanillaRecipes);
    }

    // Методы для очистки и сброса
    public static void clearAll() {
        categories.clear();
        allRecipes.clear();
        lockedVanillaRecipes.clear();
    }

    // Дополнительные вспомогательные методы
    public static boolean categoryExists(String categoryName) {
        return categories.containsKey(categoryName);
    }

    public static int getTotalRecipeCount() {
        return allRecipes.size();
    }

    public static List<CustomRecipe> getRecipesByCategory(String categoryName) {
        RecipeCategory category = categories.get(categoryName);
        return category != null ? category.getRecipes() : new ArrayList<>();
    }

    public static void updateRecipe(CustomRecipe oldRecipe, CustomRecipe newRecipe) {
        int index = allRecipes.indexOf(oldRecipe);
        if (index != -1) {
            allRecipes.set(index, newRecipe);
            RecipeCategory oldCategory = categories.get(oldRecipe.getCategory());
            RecipeCategory newCategory = categories.get(newRecipe.getCategory());

            if (oldCategory != null) {
                oldCategory.removeRecipe(oldRecipe);
            }
            if (newCategory == null) {
                newCategory = new RecipeCategory(newRecipe.getCategory());
                addCategory(newCategory);
            }
            newCategory.addRecipe(newRecipe);
        }
    }

    // Метод для проверки, разблокирован ли рецепт для игрока
    public static boolean isRecipeUnlockedForPlayer(String recipeName, String playerUUID) {
        // Здесь должна быть реализация проверки разблокировки рецепта для конкретного игрока
        // Это может потребовать дополнительной структуры данных или интеграции с системой сохранения прогресса игрока
        return false;
    }
}
