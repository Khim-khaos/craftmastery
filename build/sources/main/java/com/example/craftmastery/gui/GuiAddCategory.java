package com.example.craftmastery.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiAddCategory extends GuiScreen {
    private GuiCraftMastery parentScreen;
    private GuiTextField categoryNameField;
    private GuiButton addButton;
    private GuiButton cancelButton;

    public GuiAddCategory(GuiCraftMastery parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        categoryNameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 20, 200, 20);
        categoryNameField.setFocused(true);

        addButton = this.addButton(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 10, 95, 20, "Add"));
        cancelButton = this.addButton(new GuiButton(2, this.width / 2 + 5, this.height / 2 + 10, 95, 20, "Cancel"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Add New Category", this.width / 2, this.height / 2 - 40, 0xFFFFFF);
        categoryNameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (categoryNameField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        categoryNameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == addButton) {
            String categoryName = categoryNameField.getText().trim();
            if (!categoryName.isEmpty()) {
                parentScreen.addCategory(categoryName);
                mc.displayGuiScreen(parentScreen);
            }
        } else if (button == cancelButton) {
            mc.displayGuiScreen(parentScreen);
        }
    }
}
