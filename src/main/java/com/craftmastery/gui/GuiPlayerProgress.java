package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiPlayerProgress extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/player_progress.png");
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    private EntityPlayer player;
    private PlayerData playerData;

    public GuiPlayerProgress(EntityPlayer player) {
        this.player = player;
        this.playerData = PlayerDataManager.getInstance().getPlayerData(player);
    }

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        // Add a button to open specializations menu
        this.buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 140, 156, 20, I18n.format("gui.craftmastery.specializations")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        super.drawScreen(mouseX, mouseY, partialTicks);

        String levelText = I18n.format("gui.craftmastery.level", playerData.getLevel());
        String expText = I18n.format("gui.craftmastery.experience", playerData.getExperience(), playerData.getExpForNextLevel());
        String lpText = I18n.format("gui.craftmastery.learningpoints", playerData.getLearningPoints());

        this.fontRenderer.drawString(levelText, guiLeft + 10, guiTop + 10, 4210752);
        this.fontRenderer.drawString(expText, guiLeft + 10, guiTop + 30, 4210752);
        this.fontRenderer.drawString(lpText, guiLeft + 10, guiTop + 50, 4210752);

        // Draw experience progress bar
        int expBarWidth = 156;
        int expBarHeight = 5;
        int expBarX = guiLeft + 10;
        int expBarY = guiTop + 45;
        float expProgress = (float) playerData.getExperience() / playerData.getExpForNextLevel();
        int filledWidth = (int) (expBarWidth * expProgress);

        this.drawRect(expBarX, expBarY, expBarX + expBarWidth, expBarY + expBarHeight, 0xFF555555);
        this.drawRect(expBarX, expBarY, expBarX + filledWidth, expBarY + expBarHeight, 0xFF00FF00);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            // Open specializations menu
            // You'll implement this later when you create the specializations system
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
