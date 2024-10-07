package com.example.craftmastery.recipe;

import com.example.craftmastery.CraftMastery;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

public class RecipeManager {
    private static final Map<RecipeCategory, List<CustomRecipe>> recipesByCategory = new EnumMap<>(RecipeCategory.class);
    private static final List<CustomRecipe> allRecipes = new ArrayList<>();

    public static void initializeRecipes() {
        for (RecipeCategory category : RecipeCategory.values()) {
            recipesByCategory.put(category, new ArrayList<>());
        }

        // Magical Recipes
        addCustomRecipe(new CustomRecipe(
                "Enchanted Diamond Sword",
                new ItemStack(Items.DIAMOND_SWORD),
                Arrays.asList(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.BLAZE_POWDER)),
                20, // pointCost
                10, // pointReward
                50, // expReward
                RecipeCategory.MAGICAL,
                3  // difficultyLevel
        ));

        addCustomRecipe(new CustomRecipe(
                "Magical Golden Apple",
                new ItemStack(Items.GOLDEN_APPLE, 1, 1), // Enchanted Golden Apple
                Arrays.asList(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.GOLD_INGOT, 4), new ItemStack(Items.GLOWSTONE_DUST, 2)),
                30, // pointCost
                15, // pointReward
                75, // expReward
                RecipeCategory.MAGICAL,
                4  // difficultyLevel
        ));

        // Technical Recipes
        addCustomRecipe(new CustomRecipe(
                "Advanced Piston",
                new ItemStack(Blocks.PISTON, 2),
                Arrays.asList(new ItemStack(Blocks.PISTON), new ItemStack(Items.REDSTONE), new ItemStack(Items.IRON_INGOT)),
                15, // pointCost
                8, // pointReward
                40, // expReward
                RecipeCategory.TECHNICAL,
                2  // difficultyLevel
        ));

        addCustomRecipe(new CustomRecipe(
                "Efficient Furnace",
                new ItemStack(Blocks.FURNACE),
                Arrays.asList(new ItemStack(Blocks.FURNACE), new ItemStack(Items.REDSTONE, 4), new ItemStack(Blocks.IRON_BLOCK)),
                25, // pointCost
                12, // pointReward
                60, // expReward
                RecipeCategory.TECHNICAL,
                3  // difficultyLevel
        ));

        // Ordinary Recipes
        addCustomRecipe(new CustomRecipe(
                "Super Bread",
                new ItemStack(Items.BREAD, 3),
                Arrays.asList(new ItemStack(Items.WHEAT, 3), new ItemStack(Items.SUGAR)),
                10, // pointCost
                5, // pointReward
                30, // expReward
                RecipeCategory.ORDINARY,
                1  // difficultyLevel
        ));

        addCustomRecipe(new CustomRecipe(
                "Reinforced Armor",
                new ItemStack(Items.IRON_CHESTPLATE),
                Arrays.asList(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.LEATHER, 4), new ItemStack(Items.STRING, 2)),
                20, // pointCost
                10, // pointReward
                45, // expReward
                RecipeCategory.ORDINARY,
                2  // difficultyLevel
        ));
    }

    public static void addCustomRecipe(CustomRecipe recipe) {
        recipesByCategory.get(recipe.getCategory()).add(recipe);
        allRecipes.add(recipe);
    }

    public static void registerRecipes(IForgeRegistry<IRecipe> registry) {
        for (CustomRecipe recipe : allRecipes) {
            IRecipe iRecipe = new CustomRecipeWrapper(recipe);
            ResourceLocation registryName = new ResourceLocation(CraftMastery.MODID, recipe.getName().toLowerCase().replace(' ', '_'));
            registry.register(iRecipe.setRegistryName(registryName));
        }
    }

    public static CustomRecipe getRecipeByOutput(ItemStack output) {
        for (CustomRecipe recipe : allRecipes) {
            if (ItemStack.areItemStacksEqual(recipe.getOutput(), output)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<CustomRecipe> getAllRecipes() {
        return new ArrayList<>(allRecipes);
    }

    public static List<CustomRecipe> getRecipesByCategory(RecipeCategory category) {
        return new ArrayList<>(recipesByCategory.get(category));
    }

    public static CustomRecipe getRecipeByName(String name) {
        for (CustomRecipe recipe : allRecipes) {
            if (recipe.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    public static int getRecipeCount() {
        return allRecipes.size();
    }

    public static int getRecipeCountByCategory(RecipeCategory category) {
        return recipesByCategory.get(category).size();
    }
}
