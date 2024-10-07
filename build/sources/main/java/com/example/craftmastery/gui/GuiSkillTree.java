package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.player.PlayerProgress;
import com.example.craftmastery.player.PlayerProgressProvider;
import com.example.craftmastery.recipe.CustomRecipe;
import com.example.craftmastery.recipe.RecipeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class GuiSkillTree extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/skill_tree.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 202;

    private final EntityPlayer player;
    private final PlayerProgress playerProgress;
    private List<CustomRecipe> recipes;

    public GuiSkillTree(EntityPlayer player) {
        this.player = player;
        this.playerProgress = player.getCapability(PlayerProgressProvider.PLAYER_PROGRESS_CAPABILITY, null);
        this.recipes = RecipeManager.getAllRecipes();
    }

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;

        for (int i = 0; i < recipes.size(); i++) {
            CustomRecipe recipe = recipes.get(i);
            int x = guiLeft + 10 + (i % 5) * 50;
            int y = guiTop + 60 + (i / 5) * 50;
            buttonList.add(new GuiButton(i, x, y, 40, 40, ""));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (width - GUI_WIDTH) / 2;
        int guiTop = (height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        fontRenderer.drawString("Skill Tree", guiLeft + 8, guiTop + 6, 4210752);
        fontRenderer.drawString("Level: " + playerProgress.getLevel(), guiLeft + 8, guiTop + 18, 4210752);
        fontRenderer.drawString("XP: " + playerProgress.getExperience() + " / " + playerProgress.getExpNeededForNextLevel(), guiLeft + 8, guiTop + 30, 4210752);
        fontRenderer.drawString("Craft Points: " + playerProgress.getCraftPoints(), guiLeft + 8, guiTop + 42, 4210752);

        for (int i = 0; i < recipes.size(); i++) {
            CustomRecipe recipe = recipes.get(i);
            GuiButton button = buttonList.get(i);
            boolean isUnlocked = playerProgress.isRecipeUnlocked(recipe);
            button.enabled = !isUnlocked && playerProgress.getCraftPoints() >= recipe.getPointCost();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/items/" + recipe.getOutput().getItem().getRegistryName().getPath() + ".png"));
            drawTexturedModalRect(button.x + 12, button.y + 2, 0, 0, 16, 16);

            if (isUnlocked) {
                drawRect(button.x, button.y, button.x + button.width, button.y + button.height, 0x8000FF00);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int i = 0; i < recipes.size(); i++) {
            CustomRecipe recipe = recipes.get(i);
            GuiButton button = buttonList.get(i);
            if (isMouseOver(button, mouseX, mouseY)) {
                drawHoveringText(recipe.getName() + " (Cost: " + recipe.getPointCost() + ")", mouseX, mouseY);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < recipes.size()) {
            CustomRecipe recipe = recipes.get(button.id);
            if (playerProgress.getCraftPoints() >= recipe.getPointCost() && !playerProgress.isRecipeUnlocked(recipe)) {
                playerProgress.unlockRecipe(recipe);
                playerProgress.spendCraftPoints(recipe.getPointCost());
                button.enabled = false;
            }
        }
    }

    private boolean isMouseOver(GuiButton button, int mouseX, int mouseY) {
        return mouseX >= button.x && mouseX < button.x + button.width &&
                mouseY >= button.y && mouseY < button.y + button.height;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
