package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.crafting.CraftManager;
import com.craftmastery.crafting.CraftUnlockManager;
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
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;
    private static final int RECIPES_PER_PAGE = 5;

    private final EntityPlayer player;
    private final List<IRecipe> availableRecipes;
    private int currentPage = 0;
    private final String recipeType;

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
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        this.buttonList.clear();

        for (int i = 0; i < RECIPES_PER_PAGE; i++) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + i;
            if (recipeIndex < availableRecipes.size()) {
                IRecipe recipe = availableRecipes.get(recipeIndex);
                String recipeId = recipe.getRegistryName().toString();
                int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);
                this.buttonList.add(new GuiButton(i, guiLeft + 10, guiTop + 20 + i * 30, 156, 20,
                        recipe.getRecipeOutput().getDisplayName() + " (" + I18n.format("gui.craftmastery.cost", cost) + ")"));
            }
        }

        this.buttonList.add(new GuiButton(100, guiLeft + 10, guiTop + 140, 20, 20, "<"));
        this.buttonList.add(new GuiButton(101, guiLeft + 146, guiTop + 140, 20, 20, ">"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < RECIPES_PER_PAGE) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + button.id;
            if (recipeIndex < availableRecipes.size()) {
                IRecipe recipe = availableRecipes.get(recipeIndex);
                String recipeId = recipe.getRegistryName().toString();
                if (CraftUnlockManager.getInstance().unlockRecipe(player, recipeId)) {
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
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        super.drawScreen(mouseX, mouseY, partialTicks);

        String title = I18n.format("gui.craftmastery.recipebook." + recipeType);
        this.fontRenderer.drawString(title, guiLeft + (GUI_WIDTH - this.fontRenderer.getStringWidth(title)) / 2, guiTop + 6, 4210752);

        String learningPoints = I18n.format("gui.craftmastery.learningpoints",
                PlayerDataManager.getInstance().getPlayerData(player).getLearningPoints());
        this.fontRenderer.drawString(learningPoints, guiLeft + 8, guiTop + 154, 4210752);

        for (GuiButton button : this.buttonList) {
            if (button.id >= 0 && button.id < RECIPES_PER_PAGE) {
                int recipeIndex = currentPage * RECIPES_PER_PAGE + button.id;
                if (recipeIndex < availableRecipes.size()) {
                    IRecipe recipe = availableRecipes.get(recipeIndex);
                    String recipeId = recipe.getRegistryName().toString();
                    int cost = CraftManager.getInstance().getRecipeLearningCost(recipeId);
                    button.enabled = PlayerDataManager.getInstance().getPlayerData(player).getLearningPoints() >= cost;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
