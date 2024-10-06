package com.example.craftmastery.integration;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        if (!CraftMastery.config.enableJEIIntegration) {
            return;
        }

        List<IRecipeWrapper> recipeWrappers = new ArrayList<>();

        for (RecipeWrapper recipeWrapper : CraftingManager.getInstance().getAllRecipes()) {
            IRecipe recipe = recipeWrapper.getRecipe();
            if (recipe != null) {
                recipeWrappers.add(new JEIRecipeWrapper(recipeWrapper));
            }
        }

        registry.addRecipes(recipeWrappers, VanillaRecipeCategoryUid.CRAFTING);
    }

    private static class JEIRecipeWrapper implements IRecipeWrapper {
        private final RecipeWrapper recipeWrapper;

        JEIRecipeWrapper(RecipeWrapper recipeWrapper) {
            this.recipeWrapper = recipeWrapper;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            IRecipe recipe = recipeWrapper.getRecipe();
            List<ItemStack> inputs = new ArrayList<>();
            for (Object input : recipe.getIngredients()) {
                if (input instanceof ItemStack) {
                    inputs.add((ItemStack) input);
                }
            }
            ingredients.setInputs(ItemStack.class, inputs);
            ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
        }
    }
}
