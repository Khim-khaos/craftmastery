package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.specialization.Specialization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSpecializationDetails extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/specialization_details.png");
    private Specialization specialization;
    private List<String> recipes;
    private int currentPage = 0;
    private static final int RECIPES_PER_PAGE = 5;

    public GuiSpecializationDetails(Specialization specialization) {
        this.specialization = specialization;
        this.recipes = new ArrayList<>(specialization.getRecipeIds());
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;

        // Add recipe buttons
        for (int index = 0; index < RECIPES_PER_PAGE; index++) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + index;
            if (recipeIndex < recipes.size()) {
                String recipeId = recipes.get(recipeIndex);
                this.buttonList.add(new GuiButton(index, i + 10, j + 40 + index * 25, 150, 20, recipeId));
            }
        }

        // Add navigation buttons
        this.buttonList.add(new GuiButton(100, i + 10, j + 140, 20, 20, "<"));
        this.buttonList.add(new GuiButton(101, i + 218, j + 140, 20, 20, ">"));

        // Add back button
        this.buttonList.add(new GuiButton(102, i + 10, j + 10, 50, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < RECIPES_PER_PAGE) {
            int recipeIndex = currentPage * RECIPES_PER_PAGE + button.id;
            if (recipeIndex < recipes.size()) {
                String selectedRecipe = recipes.get(recipeIndex);
                // TODO: Open recipe details GUI
            }
        } else if (button.id == 100) {
            if (currentPage > 0) {
                currentPage--;
                initGui();
            }
        } else if (button.id == 101) {
            if ((currentPage + 1) * RECIPES_PER_PAGE < recipes.size()) {
                currentPage++;
                initGui();
            }
        } else if (button.id == 102) {
            mc.displayGuiScreen(new GuiSpecializationManager());
        }
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

        this.fontRenderer.drawString(specialization.getName(), i + 10, j + 15, 0x404040);
        this.fontRenderer.drawString("Type: " + specialization.getType(), i + 10, j + 30, 0x404040);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
