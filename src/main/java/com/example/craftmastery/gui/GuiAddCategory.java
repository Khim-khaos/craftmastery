package com.example.craftmastery.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAddCategory extends GuiScreen {
    private final GuiCraftMastery parentScreen;
    private GuiTextField categoryNameField;
    private GuiButton addButton;
    private GuiButton cancelButton;

    public GuiAddCategory(GuiCraftMastery parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        categoryNameField = new GuiTextField(0, fontRenderer, width / 2 - 100, height / 2 - 20, 200, 20);
        categoryNameField.setFocused(true);
        categoryNameField.setMaxStringLength(50);

        addButton = addButton(new GuiButton(1, width / 2 - 100, height / 2 + 10, 98, 20, I18n.format("gui.craftmastery.add")));
        cancelButton = addButton(new GuiButton(2, width / 2 + 2, height / 2 + 10, 98, 20, I18n.format("gui.cancel")));
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

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        } else {
            categoryNameField.textboxKeyTyped(typedChar, keyCode);
            addButton.enabled = !categoryNameField.getText().trim().isEmpty();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.format("gui.craftmastery.add_category"), width / 2, height / 2 - 40, 0xFFFFFF);
        categoryNameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        categoryNameField.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
