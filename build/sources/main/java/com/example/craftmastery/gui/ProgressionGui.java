package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ProgressionGui extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/progression.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;

    private final EntityPlayer player;
    private final PlayerProgression progression;

    private GuiButton recipesButton;
    private GuiButton upgradesButton;
    private GuiButton recipeTreeButton;

    private int guiLeft;
    private int guiTop;

    public ProgressionGui(EntityPlayer player) {
        this.player = player;
        this.progression = ProgressionManager.getInstance().getPlayerProgression(player);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - GUI_WIDTH) / 2;
        guiTop = (this.height - GUI_HEIGHT) / 2;

        recipesButton = new GuiButton(0, guiLeft + 20, guiTop + 60, 100, 20, I18n.format("gui.craftmastery.recipes"));
        upgradesButton = new GuiButton(1, guiLeft + 20, guiTop + 90, 100, 20, I18n.format("gui.craftmastery.upgrades"));
        recipeTreeButton = new GuiButton(2, guiLeft + 20, guiTop + 120, 100, 20, I18n.format("gui.craftmastery.recipe_tree"));

        this.buttonList.add(recipesButton);
        this.buttonList.add(upgradesButton);
        this.buttonList.add(recipeTreeButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String title = I18n.format("gui.craftmastery.progression");
        this.fontRenderer.drawString(title, guiLeft + (GUI_WIDTH - this.fontRenderer.getStringWidth(title)) / 2, guiTop + 10, 0x404040);

        String levelText = I18n.format("gui.craftmastery.level", progression.getLevel());
        this.fontRenderer.drawString(levelText, guiLeft + 20, guiTop + 30, 0x404040);

        String experienceText = I18n.format("gui.craftmastery.experience", progression.getExperience(), progression.getExperienceToNextLevel());
        this.fontRenderer.drawString(experienceText, guiLeft + 20, guiTop + 40, 0x404040);

        String craftPointsText = I18n.format("gui.craftmastery.craft_points", progression.getCraftPoints());
        this.fontRenderer.drawString(craftPointsText, guiLeft + 140, guiTop + 30, 0x404040);

        String resetPointsText = I18n.format("gui.craftmastery.reset_points", progression.getResetPoints());
        this.fontRenderer.drawString(resetPointsText, guiLeft + 140, guiTop + 40, 0x404040);

        // Draw experience bar
        int barWidth = 200;
        int barHeight = 5;
        int barX = guiLeft + (GUI_WIDTH - barWidth) / 2;
        int barY = guiTop + 55;
        float progress = (float) progression.getExperience() / progression.getExperienceToNextLevel();

        drawRect(barX, barY, barX + barWidth, barY + barHeight, 0xFF555555);
        drawRect(barX, barY, barX + (int)(barWidth * progress), barY + barHeight, 0xFF00FF00);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == recipesButton) {
            mc.displayGuiScreen(new RecipesGui(player));
        } else if (button == upgradesButton) {
            mc.displayGuiScreen(new UpgradesGui(player));
        } else if (button == recipeTreeButton) {
            mc.displayGuiScreen(new RecipeTreeGui(player));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
