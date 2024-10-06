package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.progression.Upgrade;
import com.example.craftmastery.network.NetworkHandler;
import com.example.craftmastery.network.messages.UnlockUpgradeMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpgradesGui extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/upgrades.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;
    private static final int UPGRADES_PER_PAGE = 5;

    private final EntityPlayer player;
    private final PlayerProgression progression;
    private final List<Upgrade> availableUpgrades;
    private final List<GuiButton> unlockButtons;
    private GuiButton nextPageButton;
    private GuiButton prevPageButton;
    private GuiButton backButton;
    private int currentPage = 0;
    private int guiLeft;
    private int guiTop;

    public UpgradesGui(EntityPlayer player) {
        this.player = player;
        this.progression = ProgressionManager.getInstance().getPlayerProgression(player);
        this.availableUpgrades = ProgressionManager.getInstance().getAvailableUpgrades(progression);
        this.unlockButtons = new ArrayList<>();
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - GUI_WIDTH) / 2;
        guiTop = (this.height - GUI_HEIGHT) / 2;

        backButton = new GuiButton(0, guiLeft + 10, guiTop + GUI_HEIGHT - 30, 100, 20, I18n.format("gui.craftmastery.back"));
        this.buttonList.add(backButton);

        prevPageButton = new GuiButton(1, guiLeft + 120, guiTop + GUI_HEIGHT - 30, 20, 20, "<");
        nextPageButton = new GuiButton(2, guiLeft + 145, guiTop + GUI_HEIGHT - 30, 20, 20, ">");
        this.buttonList.add(prevPageButton);
        this.buttonList.add(nextPageButton);

        updatePageButtons();
        updateUpgradeButtons();
    }

    private void updatePageButtons() {
        prevPageButton.enabled = currentPage > 0;
        nextPageButton.enabled = (currentPage + 1) * UPGRADES_PER_PAGE < availableUpgrades.size();
    }

    private void updateUpgradeButtons() {
        unlockButtons.clear();
        this.buttonList.removeIf(button -> button.id >= 100);

        int startIndex = currentPage * UPGRADES_PER_PAGE;
        int endIndex = Math.min(startIndex + UPGRADES_PER_PAGE, availableUpgrades.size());

        for (int i = startIndex; i < endIndex; i++) {
            Upgrade upgrade = availableUpgrades.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            GuiButton unlockButton = new GuiButton(100 + i, guiLeft + 180, guiTop + yOffset, 60, 20,
                    progression.isUpgradeUnlocked(upgrade.getId()) ? I18n.format("gui.craftmastery.unlocked") : I18n.format("gui.craftmastery.unlock"));
            unlockButton.enabled = !progression.isUpgradeUnlocked(upgrade.getId()) && progression.getCraftPoints() >= upgrade.getCost();
            this.buttonList.add(unlockButton);
            unlockButtons.add(unlockButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String title = I18n.format("gui.craftmastery.upgrades");
        this.fontRenderer.drawString(title, guiLeft + (GUI_WIDTH - this.fontRenderer.getStringWidth(title)) / 2, guiTop + 10, 0x404040);

        String craftPoints = I18n.format("gui.craftmastery.craft_points", progression.getCraftPoints());
        this.fontRenderer.drawString(craftPoints, guiLeft + 10, guiTop + 20, 0x404040);

        int startIndex = currentPage * UPGRADES_PER_PAGE;
        int endIndex = Math.min(startIndex + UPGRADES_PER_PAGE, availableUpgrades.size());

        for (int i = startIndex; i < endIndex; i++) {
            Upgrade upgrade = availableUpgrades.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            String upgradeName = I18n.format("upgrade." + upgrade.getId() + ".name");
            this.fontRenderer.drawString(upgradeName, guiLeft + 10, guiTop + yOffset + 4, 0x404040);

            String upgradeCost = I18n.format("gui.craftmastery.upgrade_cost", upgrade.getCost());
            this.fontRenderer.drawString(upgradeCost, guiLeft + 10, guiTop + yOffset + 14, 0x808080);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Отрисовка всплывающих подсказок
        for (int i = startIndex; i < endIndex; i++) {
            Upgrade upgrade = availableUpgrades.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            if (mouseX >= guiLeft + 10 && mouseX <= guiLeft + 170 && mouseY >= guiTop + yOffset && mouseY <= guiTop + yOffset + 20) {
                List<String> tooltip = new ArrayList<>();
                tooltip.add(I18n.format("upgrade." + upgrade.getId() + ".description"));
                this.drawHoveringText(tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == backButton) {
            mc.displayGuiScreen(new ProgressionGui(player));
        } else if (button == prevPageButton) {
            if (currentPage > 0) {
                currentPage--;
                updatePageButtons();
                updateUpgradeButtons();
            }
        } else if (button == nextPageButton) {
            if ((currentPage + 1) * UPGRADES_PER_PAGE < availableUpgrades.size()) {
                currentPage++;
                updatePageButtons();
                updateUpgradeButtons();
            }
        } else if (unlockButtons.contains(button)) {
            int index = button.id - 100 + currentPage * UPGRADES_PER_PAGE;
            Upgrade upgrade = availableUpgrades.get(index);
            if (progression.getCraftPoints() >= upgrade.getCost()) {
                NetworkHandler.INSTANCE.sendToServer(new UnlockUpgradeMessage(upgrade.getId()));
                progression.unlockUpgrade(upgrade.getId(), upgrade.getCost());
                updateUpgradeButtons();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
