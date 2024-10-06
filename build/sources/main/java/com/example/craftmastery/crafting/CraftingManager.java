package com.example.craftmastery.crafting;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.config.ModConfig;
import com.example.craftmastery.progression.PlayerProgression;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class CraftingManager {
    private static CraftingManager instance;
    private final Map<String, RecipeWrapper> recipes;

    private CraftingManager() {
        this.recipes = new HashMap<>();
    }

    public static CraftingManager getInstance() {
        if (instance == null) {
            instance = new CraftingManager();
        }
        return instance;
    }

    public void initializeRecipes() {
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            String recipeId = recipe.getRegistryName().toString();
            int requiredLevel = calculateRequiredLevel(recipe);
            int pointCost = calculatePointCost(recipe);
            double difficulty = calculateDifficulty(recipe);

            RecipeWrapper wrapper = new RecipeWrapper.Builder(recipe)
                    .requiredLevel(requiredLevel)
                    .pointCost(pointCost)
                    .difficulty(difficulty)
                    .build();

            recipes.put(recipeId, wrapper);
        }

        setupRecipePrerequisites();
        CraftMastery.logger.info("Initialized {} recipes", recipes.size());
    }

    private int calculateRequiredLevel(IRecipe recipe) {
        // Пример простого расчета уровня на основе количества ингредиентов
        return Math.max(1, recipe.getIngredients().size());
    }

    private int calculatePointCost(IRecipe recipe) {
        // Пример расчета стоимости на основе количества ингредиентов и результата
        return Math.max(1, recipe.getIngredients().size() + recipe.getRecipeOutput().getCount());
    }

    private double calculateDifficulty(IRecipe recipe) {
        // Пример расчета сложности
        return Math.max(1.0, recipe.getIngredients().size() * 0.5);
    }

    private void setupRecipePrerequisites() {
        // Пример установки предварительных требований
        // Здесь вы можете реализовать более сложную логику
        for (RecipeWrapper recipe : recipes.values()) {
            if (recipe.getRequiredLevel() > 1) {
                List<RecipeWrapper> possiblePrereqs = recipes.values().stream()
                        .filter(r -> r.getRequiredLevel() < recipe.getRequiredLevel())
                        .collect(Collectors.toList());
                if (!possiblePrereqs.isEmpty()) {
                    RecipeWrapper prereq = possiblePrereqs.get(new Random().nextInt(possiblePrereqs.size()));
                    recipe.addPrerequisite(prereq.getId());
                }
            }
        }
    }

    public List<RecipeWrapper> getAvailableRecipes(PlayerProgression progression) {
        return recipes.values().stream()
                .filter(recipe -> recipe.getRequiredLevel() <= progression.getLevel())
                .collect(Collectors.toList());
    }

    public List<RecipeWrapper> getUnlockedRecipes(PlayerProgression progression) {
        return recipes.values().stream()
                .filter(recipe -> progression.isRecipeUnlocked(recipe.getId()))
                .collect(Collectors.toList());
    }

    public RecipeWrapper getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }

    public boolean isRecipeAvailable(String recipeId, PlayerProgression progression) {
        RecipeWrapper recipe = recipes.get(recipeId);
        if (recipe == null) {
            return false;
        }
        return recipe.getRequiredLevel() <= progression.getLevel() &&
                progression.getCraftPoints() >= recipe.getPointCost() &&
                (recipe.getPrerequisites().isEmpty() ||
                        recipe.getPrerequisites().stream().allMatch(progression::isRecipeUnlocked));
    }

    public RecipeWrapper getRecipeForResult(ItemStack result) {
        for (RecipeWrapper wrapper : recipes.values()) {
            if (ItemStack.areItemStacksEqual(wrapper.getRecipe().getRecipeOutput(), result)) {
                return wrapper;
            }
        }
        return null;
    }

    public Collection<RecipeWrapper> getAllRecipes() {
        return recipes.values();
    }
}
