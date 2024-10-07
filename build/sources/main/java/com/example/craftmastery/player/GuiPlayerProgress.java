package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.player.PlayerProgress;
import com.example.craftmastery.player.PlayerProgressProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPlayerProgress extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/player_progress.png");
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    private final EntityPlayer player;
    private final PlayerProgress playerProgress;

    public GuiPlayerProgress(EntityPlayer player) {
        this.player = player;
        this.playerProgress = player.getCapability(PlayerProgressProvider.PLAYER_PROGRESS_CAPABILITY, null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String levelText = "Level: " + playerProgress.getLevel();
        String expText = "XP: " + playerProgress.getExperience() + " / " + playerProgress.getExpNeededForNextLevel();
        String pointsText = "Craft Points: " + playerProgress.getCraftPoints();

        fontRenderer.drawString(levelText, guiLeft + 10, guiTop + 10, 0x404040);
        fontRenderer.drawString(expText, guiLeft + 10, guiTop + 25, 0x404040);
        fontRenderer.drawString(pointsText, guiLeft + 10, guiTop + 40, 0x404040);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
