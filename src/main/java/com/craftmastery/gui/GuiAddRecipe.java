package com.craftmastery.gui;

import com.craftmastery.crafting.CraftManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAddRecipe extends GuiScreen {
    private GuiTextField recipeIdField;
    private GuiTextField specializationField;
    private GuiTextField learningCostField;
    private GuiButton addButton;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        int centerX = width / 2;
        int centerY = height / 2;

        recipeIdField = new GuiTextField(0, fontRenderer, centerX - 100, centerY - 40, 200, 20);
        recipeIdField.setFocused(true);
        recipeIdField.setCanLoseFocus(true);
        recipeIdField.setMaxStringLength(100);

        specializationField = new GuiTextField(1, fontRenderer, centerX - 100, centerY, 200, 20);
        specializationField.setCanLoseFocus(true);
        specializationField.setMaxStringLength(50);

        learningCostField = new GuiTextField(2, fontRenderer, centerX - 100, centerY + 40, 200, 20);
        learningCostField.setCanLoseFocus(true);
        learningCostField.setMaxStringLength(10);

        addButton = new GuiButton(0, centerX - 50, centerY + 80, 100, 20, I18n.format("gui.addrecipe.add"));
        buttonList.add(addButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        recipeIdField.updateCursorCounter();
        specializationField.updateCursorCounter();
        learningCostField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }

        recipeIdField.textboxKeyTyped(typedChar, keyCode);
        specializationField.textboxKeyTyped(typedChar, keyCode);
        learningCostField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_RETURN) {
            actionPerformed(addButton);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        recipeIdField.mouseClicked(mouseX, mouseY, mouseButton);
        specializationField.mouseClicked(mouseX, mouseY, mouseButton);
        learningCostField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.format("gui.addrecipe.title"), width / 2, 20, 0xFFFFFF);

        recipeIdField.drawTextBox();
        specializationField.drawTextBox();
        learningCostField.drawTextBox();

        drawString(fontRenderer, I18n.format("gui.addrecipe.recipeid"), recipeIdField.x, recipeIdField.y - 15, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.addrecipe.specialization"), specializationField.x, specializationField.y - 15, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.addrecipe.learningcost"), learningCostField.x, learningCostField.y - 15, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            String recipeId = recipeIdField.getText();
            String specializationId = specializationField.getText();
            int learningCost;
            try {
                learningCost = Integer.parseInt(learningCostField.getText());
            } catch (NumberFormatException e) {
                // Handle error if not a number
                return;
            }

            CraftManager.getInstance().addRecipeToSpecialization(recipeId, specializationId, learningCost);
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
