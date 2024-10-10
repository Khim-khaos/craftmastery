package com.craftmastery.gui;

import com.craftmastery.crafting.CraftManager;
import com.craftmastery.item.ItemRecipeBook;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiRecipeBook extends GuiScreen {
    private EntityPlayer player;
    private List<IRecipe> availableRecipes;
    private int currentPage = 0;
    private static final int RECIPES_PER_PAGE = 5;
    private String recipeType;

    public GuiRecipeBook(EntityPlayer player, ItemStack recipeBook) {
        this.player = player;
        this.recipeType = ((ItemRecipeBook) recipeBook.getItem()).getRecipeType();
        this.availableRecipes = new ArrayList<>();

        SpecializationManager specManager = SpecializationManager.getInstance();
        CraftManager craftManager = CraftManager.getInstance();

        for (IRecipe recipe : craftManager.getRecipesByType(recipeType)) {
            String recipeId = recipe.getRegistryName().toString();
            if (specManager.isRecipeAllowedForPlayer(player, recipeId) &&
                    !craftManager.isRecipeUnlocked(player, recipeId)) {
                availableRecipes.add(recipe);
            }
        }
    }

    // ... остальные методы остаются без изменений ...
}
