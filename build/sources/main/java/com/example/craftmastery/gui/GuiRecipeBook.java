package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.recipe.CustomRecipe;
import com.example.craftmastery.recipe.RecipeCategory;
import com.example.craftmastery.recipe.RecipeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class GuiRecipeBook extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/recipe_book.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 202;

    private RecipeCategory currentCategory = RecipeCategory.MAGICAL;
    private int currentPage = 0;
    private static final int RECIPES_PER_PAGE = 5;

    private GuiButton prevButton;
    private GuiButton nextButton;

    @Override
    public void initGui() {
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;

        buttonList.clear();

        // Category buttons
        buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 20, 80, 20, RecipeCategory.MAGICAL.getDisplayName()));
        buttonList.add(new GuiButton(1, guiLeft + 10, guiTop + 45, 80, 20, RecipeCategory.TECHNICAL.getDisplayName()));
        buttonList.add(new GuiButton(2, guiLeft + 10, guiTop + 70, 80, 20, RecipeCategory.ORDINARY.getDisplayName()));

        // Navigation buttons
        prevButton = new GuiButton(100, guiLeft + 120, guiTop + 180, 20, 20, "<");
        nextButton = new GuiButton(101, guiLeft + 220, guiTop + 180, 20, 20, ">");
        buttonList.add(prevButton);
        buttonList.add(nextButton);

        updateButtonStates();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        fontRenderer.drawString(I18n.format("gui.recipe_book.title"), guiLeft + 8, guiTop + 6, 4210752);

        List<CustomRecipe> recipes = RecipeManager.getRecipesByCategory(currentCategory);
        int startIndex = currentPage * RECIPES_PER_PAGE;
        int endIndex = Math.min(startIndex + RECIPES_PER_PAGE, recipes.size());

        for (int i = startIndex; i < endIndex; i++) {
            CustomRecipe recipe = recipes.get(i);
            int recipeY = guiTop + 30 + (i - startIndex) * 30;

            fontRenderer.drawString(recipe.getName(), guiLeft + 100, recipeY, 4210752);
            fontRenderer.drawString("Cost: " + recipe.getPointCost(), guiLeft + 100, recipeY + 10, 4210752);
            fontRenderer.drawString("Difficulty: " + recipe.getDifficultyLevel(), guiLeft + 100, recipeY + 20, 4210752);

            // Draw recipe output item
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f, 1.5f, 1.5f);
            itemRender.renderItemAndEffectIntoGUI(recipe.getOutput(), (int)((guiLeft + 210) / 1.5f), (int)(recipeY / 1.5f));
            GlStateManager.popMatrix();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id < RecipeCategory.values().length) {
            currentCategory = RecipeCategory.values()[button.id];
            currentPage = 0;
        } else if (button == prevButton) {
            if (currentPage > 0) currentPage--;
        } else if (button == nextButton) {
            List<CustomRecipe> recipes = RecipeManager.getRecipesByCategory(currentCategory);
            if ((currentPage + 1) * RECIPES_PER_PAGE < recipes.size()) currentPage++;
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        List<CustomRecipe> recipes = RecipeManager.getRecipesByCategory(currentCategory);
        prevButton.enabled = currentPage > 0;
        nextButton.enabled = (currentPage + 1) * RECIPES_PER_PAGE < recipes.size();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
