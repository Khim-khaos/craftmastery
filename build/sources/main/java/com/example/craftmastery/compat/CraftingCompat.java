package com.example.craftmastery.compat;

import com.example.craftmastery.crafting.RecipeWrapper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;

public class CraftingCompat {

    public static RecipeWrapper wrapRecipe(IRecipe recipe) {
        if (Loader.isModLoaded("crafttweaker") && recipe instanceof crafttweaker.api.recipes.ICraftingRecipe) {
            return wrapCraftTweakerRecipe((crafttweaker.api.recipes.ICraftingRecipe) recipe);
        }
        if (Loader.isModLoaded("thermalexpansion") && recipe instanceof cofh.thermalexpansion.util.managers.machine.CraftingManager.CraftingRecipe) {
            return wrapThermalExpansionRecipe((cofh.thermalexpansion.util.managers.machine.CraftingManager.CraftingRecipe) recipe);
        }
        if (Loader.isModLoaded("tconstruct") && recipe instanceof slimeknights.tconstruct.library.smeltery.ICastingRecipe) {
            return wrapTConstructRecipe((slimeknights.tconstruct.library.smeltery.ICastingRecipe) recipe);
        }
        // Add more mod-specific wrappers here as needed
        return new RecipeWrapper(recipe);
    }

    private static RecipeWrapper wrapCraftTweakerRecipe(crafttweaker.api.recipes.ICraftingRecipe recipe) {
        // Implement CraftTweaker-specific recipe wrapping logic
        String id = "crafttweaker_" + recipe.getName();
        int pointCost = calculatePointCost(recipe);
        int requiredLevel = calculateRequiredLevel(recipe);
        return new RecipeWrapper(id, recipe, pointCost, "crafttweaker", requiredLevel);
    }

    private static RecipeWrapper wrapThermalExpansionRecipe(cofh.thermalexpansion.util.managers.machine.CraftingManager.CraftingRecipe recipe) {
        // Implement Thermal Expansion-specific recipe wrapping logic
        String id = "thermalexpansion_" + recipe.getOutput().getUnlocalizedName();
        int pointCost = calculatePointCost(recipe);
        int requiredLevel = calculateRequiredLevel(recipe);
        return new RecipeWrapper(id, recipe, pointCost, "thermalexpansion", requiredLevel);
    }

    private static RecipeWrapper wrapTConstructRecipe(slimeknights.tconstruct.library.smeltery.ICastingRecipe recipe) {
        // Implement Tinkers Construct-specific recipe wrapping logic
        String id = "tconstruct_" + recipe.getResult().getUnlocalizedName();
        int pointCost = calculatePointCost(recipe);
        int requiredLevel = calculateRequiredLevel(recipe);
        return new RecipeWrapper(id, recipe, pointCost, "tconstruct", requiredLevel);
    }

    private static int calculatePointCost(Object recipe) {
        // Implement logic to calculate point cost based on recipe complexity
        // This is a placeholder implementation
        return 10;
    }

    private static int calculateRequiredLevel(Object recipe) {
        // Implement logic to calculate required level based on recipe complexity
        // This is a placeholder implementation
        return 1;
    }
}
