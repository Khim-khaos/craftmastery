package com.craftmastery.gui;

import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.specialization.Specialization;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Map;

public class GuiPlayerStats extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation("craftmastery", "textures/gui/player_stats.png");
    private int guiLeft;
    private int guiTop;
    private final int xSize = 176;
    private final int ySize = 166;

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        // Add buttons if needed
        // this.buttonList.add(new GuiButton(0, this.guiLeft + 10, this.guiTop + 10, 100, 20, "Button"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(mc.player);

        // Draw player level and experience
        String level = I18n.format("gui.playerstats.level", playerData.getLevel());
        this.fontRenderer.drawString(level, this.guiLeft + 10, this.guiTop + 10, 0x404040);

        String experience = I18n.format("gui.playerstats.experience", playerData.getExperience(), playerData.getExpForNextLevel());
        this.fontRenderer.drawString(experience, this.guiLeft + 10, this.guiTop + 25, 0x404040);

        // Draw learning points
        String learningPoints = I18n.format("gui.playerstats.learningpoints", playerData.getLearningPoints());
        this.fontRenderer.drawString(learningPoints, this.guiLeft + 10, this.guiTop + 40, 0x404040);

        // Draw specialization reset points
        String resetPoints = I18n.format("gui.playerstats.resetpoints", playerData.getSpecializationResetPoints());
        this.fontRenderer.drawString(resetPoints, this.guiLeft + 10, this.guiTop + 55, 0x404040);

        // Draw specializations
        this.fontRenderer.drawString(I18n.format("gui.playerstats.specializations"), this.guiLeft + 10, this.guiTop + 75, 0x404040);
        int yOffset = 90;
        for (Map.Entry<String, Integer> entry : playerData.getSpecializations().entrySet()) {
            Specialization spec = SpecializationManager.getInstance().getSpecialization(entry.getKey());
            if (spec != null) {
                String specText = spec.getName() + ": " + entry.getValue();
                this.fontRenderer.drawString(specText, this.guiLeft + 20, this.guiTop + yOffset, 0x404040);
                yOffset += 15;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // Handle button clicks
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
