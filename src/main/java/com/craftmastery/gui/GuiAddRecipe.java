package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.crafting.CraftManager;
import com.craftmastery.specialization.Specialization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiAddRecipe extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/add_recipe.png");
    private Specialization specialization;
    private GuiTextField recipeIdField;

    public GuiAddRecipe(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;

        recipeIdField = new GuiTextField(0, this.fontRenderer, i + 10, j + 50, 228, 20);
        recipeIdField.setFocused(true);

        this.buttonList.add(new GuiButton(0, i + 10, j + 80, 100, 20, "Add Recipe"));
        this.buttonList.add(new GuiButton(1, i + 138, j + 80, 100, 20, "Cancel"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            String recipeId = recipeIdField.getText().trim();
            if (!recipeId.isEmpty()) {
                CraftManager.getInstance().addRecipeToSpecialization(recipeId, specialization.getId());
                mc.displayGuiScreen(new GuiSpecializationDetails(specialization));
            }
        } else if (button.id == 1) {
            mc.displayGuiScreen(new GuiSpecializationDetails(specialization));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        recipeIdField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        recipeIdField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawString("Add Recipe to " + specialization.getName(), i + 10, j + 10, 0x404040);
        this.fontRenderer.drawString("Recipe ID:", i + 10, j + 40, 0x404040);

        recipeIdField.drawTextBox();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        recipeIdField.updateCursorCounter();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
