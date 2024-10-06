package com.example.craftmastery.gui;

import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class AdminCraftingGui extends GuiScreen {
    private GuiTextField recipeIdField;
    private GuiTextField pointCostField;
    private GuiTextField categoryIdField;
    private GuiButton addButton;

    @Override
    public void initGui() {
        super.initGui();

        int centerX = width / 2;
        int centerY = height / 2;

        recipeIdField = new GuiTextField(0, fontRenderer, centerX - 100, centerY - 40, 200, 20);
        recipeIdField.setFocused(true);

        pointCostField = new GuiTextField(1, fontRenderer, centerX - 100, centerY, 200, 20);
        categoryIdField = new GuiTextField(2, fontRenderer, centerX - 100, centerY + 40, 200, 20);

        addButton = new GuiButton(3, centerX - 50, centerY + 80, 100, 20, I18n.format("gui.craftmastery.add_recipe"));
        buttonList.add(addButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == addButton) {
            String recipeId = recipeIdField.getText();
            int pointCost = Integer.parseInt(pointCostField.getText());
            String categoryId = categoryIdField.getText();

            CraftingManager.getInstance().addRecipe(recipeId, null, pointCost, categoryId);
            // TODO: Send sync message to server
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        recipeIdField.textboxKeyTyped(typedChar, keyCode);
        pointCostField.textboxKeyTyped(typedChar, keyCode);
        categoryIdField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        recipeIdField.mouseClicked(mouseX, mouseY, mouseButton);
        pointCostField.mouseClicked(mouseX, mouseY, mouseButton);
        categoryIdField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        fontRenderer.drawString(I18n.format("gui.craftmastery.recipe_id"), recipeIdField.x, recipeIdField.y - 15, 0xFFFFFF);
        fontRenderer.drawString(I18n.format("gui.craftmastery.point_cost"), pointCostField.x, pointCostField.y - 15, 0xFFFFFF);
        fontRenderer.drawString(I18n.format("gui.craftmastery.category_id"), categoryIdField.x, categoryIdField.y - 15, 0xFFFFFF);

        recipeIdField.drawTextBox();
        pointCostField.drawTextBox();
        categoryIdField.drawTextBox();
    }
}
