package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.CraftRecipe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiCraftMastery extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/craftmastery.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;

    private List<String> categories = new ArrayList<>();
    private int currentCategory = 0;
    private List<CraftRecipe> visibleRecipes = new ArrayList<>();

    private GuiButton prevCategoryButton;
    private GuiButton nextCategoryButton;
    private GuiButton addCategoryButton;

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        prevCategoryButton = addButton(new GuiButton(0, guiLeft + 10, guiTop + 5, 20, 20, "<"));
        nextCategoryButton = addButton(new GuiButton(1, guiLeft + GUI_WIDTH - 30, guiTop + 5, 20, 20, ">"));
        addCategoryButton = addButton(new GuiButton(2, guiLeft + GUI_WIDTH - 25, guiTop - 25, 20, 20, "+"));

        categories = CraftingManager.getInstance().getCategories();
        updateVisibleRecipes();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String categoryName = categories.isEmpty() ? "No Categories" : categories.get(currentCategory);
        fontRenderer.drawString(categoryName, guiLeft + GUI_WIDTH / 2 - fontRenderer.getStringWidth(categoryName) / 2, guiTop + 10, 0x404040);

        drawRecipes(guiLeft, guiTop);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawRecipes(int guiLeft, int guiTop) {
        int recipeStartX = guiLeft + 10;
        int recipeStartY = guiTop + 30;
        int recipeSize = 32;
        int recipesPerRow = 7;

        for (int i = 0; i < visibleRecipes.size(); i++) {
            int x = recipeStartX + (i % recipesPerRow) * recipeSize;
            int y = recipeStartY + (i / recipesPerRow) * recipeSize;

            // Здесь нужно отрисовать иконку рецепта
            // Например: drawItemStack(visibleRecipes.get(i).getOutput(), x, y, null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == prevCategoryButton) {
            currentCategory = (currentCategory - 1 + categories.size()) % categories.size();
            updateVisibleRecipes();
        } else if (button == nextCategoryButton) {
            currentCategory = (currentCategory + 1) % categories.size();
            updateVisibleRecipes();
        } else if (button == addCategoryButton) {
            // Открыть диалог для добавления новой категории
            mc.displayGuiScreen(new GuiAddCategory(this));
        }
    }

    private void updateVisibleRecipes() {
        if (!categories.isEmpty()) {
            visibleRecipes = CraftingManager.getInstance().getRecipes(categories.get(currentCategory));
        } else {
            visibleRecipes.clear();
        }
    }

    public void addCategory(String categoryName) {
        CraftingManager.getInstance().addCategory(categoryName);
        categories = CraftingManager.getInstance().getCategories();
        currentCategory = categories.size() - 1;
        updateVisibleRecipes();
    }
}
