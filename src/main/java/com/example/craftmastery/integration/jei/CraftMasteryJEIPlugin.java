package com.example.craftmastery.integration.jei;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CraftMasteryJEIPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

        // Скрываем недоступные рецепты
        for (CraftRecipe recipe : CraftMastery.getCraftingManager().getAllRecipes()) {
            PlayerData playerData = PlayerDataManager.getPlayerData(Minecraft.getMinecraft().player);
            if (playerData != null && !playerData.isRecipeUnlocked(recipe) && !CraftRecipe.isItemAllowed(recipe.getOutput())) {
                blacklist.addIngredientToBlacklist(recipe.getOutput());
            }
        }
    }
}
