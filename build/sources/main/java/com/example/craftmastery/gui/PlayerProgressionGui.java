package com.example.craftmastery.gui;

import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.progression.PlayerProgression;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;

public class PlayerProgressionGui extends GuiScreen {
    private EntityPlayer player;
    private PlayerProgression progression;

    public PlayerProgressionGui(EntityPlayer player) {
        this.player = player;
        this.progression = ProgressionManager.getInstance().getPlayerProgression(player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int centerX = width / 2;
        int centerY = height / 2;

        fontRenderer.drawString(I18n.format("gui.craftmastery.player_progression"), centerX - 100, centerY - 80, 0xFFFFFF);

        int y = centerY - 60;
        for (String recipeId : progression.getUnlockedRecipes()) {
            fontRenderer.drawString(recipeId, centerX - 100, y, 0xFFFFFF);
            y += 15;
        }
    }
}
