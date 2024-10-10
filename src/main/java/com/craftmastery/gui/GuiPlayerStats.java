package com.craftmastery.gui;

import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import com.craftmastery.CraftMastery;

public class GuiPlayerStats extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/player_stats.png");
    private final EntityPlayer player;
    private final PlayerData playerData;

    public GuiPlayerStats(EntityPlayer player) {
        this.player = player;
        this.playerData = PlayerDataManager.getInstance().getPlayerData(player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);

        String levelText = "Level: " + playerData.getLevel();
        String expText = "Experience: " + playerData.getExperience() + " / " + playerData.getExpForNextLevel();
        String learningPointsText = "Learning Points: " + playerData.getLearningPoints();
        String craftResetPointsText = "Craft Reset Points: " + playerData.getCraftResetPoints();
        String specResetPointsText = "Specialization Reset Points: " + playerData.getSpecializationResetPoints();

        this.fontRenderer.drawString(levelText, i + 10, j + 10, 0x404040);
        this.fontRenderer.drawString(expText, i + 10, j + 25, 0x404040);
        this.fontRenderer.drawString(learningPointsText, i + 10, j + 40, 0x404040);
        this.fontRenderer.drawString(craftResetPointsText, i + 10, j + 55, 0x404040);
        this.fontRenderer.drawString(specResetPointsText, i + 10, j + 70, 0x404040);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
