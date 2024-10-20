package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.crafting.CraftManager;
import com.craftmastery.item.ItemRecipeBook;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiRecipeBook extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/recipe_book.png");
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

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - 176) / 2;
        int j = (this.height - 166) / 2;

        for (int index = 0; index < RECIPES_PER_PAGE; index++) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + index;
            if (recipeIndex < availableRecipes.size()) {
                IRecipe recipe = availableRecipes.get(recipeIndex);
                String recipeId = recipe.getRegistryName().toString();
                int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);
                this.buttonList.add(new GuiButton(index, i + 10, j + 20 + index * 30, 156, 20,
                        recipe.getRecipeOutput().getDisplayName() + " (" + I18n.format("gui.craftmastery.cost", cost) + ")"));
            }
        }

        this.buttonList.add(new GuiButton(100, i + 10, j + 140, 20, 20, "<"));
        this.buttonList.add(new GuiButton(101, i + 146, j + 140, 20, 20, ">"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < RECIPES_PER_PAGE) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + button.id;
            if (recipeIndex < availableRecipes.size()) {
                IRecipe recipe = availableRecipes.get(recipeIndex);
                String recipeId = recipe.getRegistryName().toString();
                if (CraftManager.getInstance().learnRecipe(player, recipeId)) {
                    button.enabled = false;
                }
            }
        } else if (button.id == 100 && currentPage > 0) {
            currentPage--;
            initGui();
        } else if (button.id == 101 && (currentPage + 1) * RECIPES_PER_PAGE < availableRecipes.size()) {
            currentPage++;
            initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - 176) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 176, 166);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawString(I18n.format("gui.craftmastery.recipebook." + recipeType), i + 8, j + 6, 4210752);
        this.fontRenderer.drawString(I18n.format("gui.craftmastery.learningpoints",
                        PlayerDataManager.getInstance().getPlayerData(player).getLearningPoints()),
                i + 8, j + 154, 4210752);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
