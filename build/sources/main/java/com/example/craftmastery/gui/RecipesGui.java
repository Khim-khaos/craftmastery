package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.network.NetworkHandler;
import com.example.craftmastery.network.messages.UnlockRecipeMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipesGui extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/recipes.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;
    private static final int RECIPES_PER_PAGE = 5;

    private final EntityPlayer player;
    private final PlayerProgression progression;
    private final List<RecipeWrapper> availableRecipes;
    private final List<GuiButton> unlockButtons;
    private final List<GuiButton> forgetButtons;
    private GuiButton nextPageButton;
    private GuiButton prevPageButton;
    private GuiButton backButton;
    private int currentPage = 0;
    private int guiLeft;
    private int guiTop;

    public RecipesGui(EntityPlayer player) {
        this.player = player;
        this.progression = ProgressionManager.getInstance().getPlayerProgression(player);
        this.availableRecipes = CraftingManager.getInstance().getAvailableRecipes(progression);
        this.unlockButtons = new ArrayList<>();
        this.forgetButtons = new ArrayList<>();
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
        updateRecipeButtons();
    }

    private void updatePageButtons() {
        prevPageButton.enabled = currentPage > 0;
        nextPageButton.enabled = (currentPage + 1) * RECIPES_PER_PAGE < availableRecipes.size();
    }

    private void updateRecipeButtons() {
        unlockButtons.clear();
        forgetButtons.clear();
        this.buttonList.removeIf(button -> button.id >= 100);

        int startIndex = currentPage * RECIPES_PER_PAGE;
        int endIndex = Math.min(startIndex + RECIPES_PER_PAGE, availableRecipes.size());

        for (int i = startIndex; i < endIndex; i++) {
            RecipeWrapper recipe = availableRecipes.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            if (!progression.isRecipeUnlocked(recipe.getId())) {
                GuiButton unlockButton = new GuiButton(100 + i, guiLeft + 180, guiTop + yOffset, 60, 20, I18n.format("gui.craftmastery.unlock"));
                unlockButton.enabled = progression.canUnlockRecipe(recipe);
                this.buttonList.add(unlockButton);
                unlockButtons.add(unlockButton);
            } else {
                GuiButton forgetButton = new GuiButton(200 + i, guiLeft + 180, guiTop + yOffset, 60, 20, I18n.format("gui.craftmastery.forget"));
                this.buttonList.add(forgetButton);
                forgetButtons.add(forgetButton);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String title = I18n.format("gui.craftmastery.recipes");
        this.fontRenderer.drawString(title, guiLeft + (GUI_WIDTH - this.fontRenderer.getStringWidth(title)) / 2, guiTop + 10, 0x404040);

        int startIndex = currentPage * RECIPES_PER_PAGE;
        int endIndex = Math.min(startIndex + RECIPES_PER_PAGE, availableRecipes.size());

        for (int i = startIndex; i < endIndex; i++) {
            RecipeWrapper recipe = availableRecipes.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            ItemStack output = recipe.getRecipe().getRecipeOutput();
            this.itemRender.renderItemAndEffectIntoGUI(output, guiLeft + 10, guiTop + yOffset);

            String recipeName = output.getDisplayName();
            this.fontRenderer.drawString(recipeName, guiLeft + 30, guiTop + yOffset + 4, 0x404040);

            String levelRequirement = I18n.format("gui.craftmastery.level_requirement", recipe.getRequiredLevel());
            this.fontRenderer.drawString(levelRequirement, guiLeft + 30, guiTop + yOffset + 14, 0x808080);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int i = startIndex; i < endIndex; i++) {
            RecipeWrapper recipe = availableRecipes.get(i);
            int yOffset = 30 + (i - startIndex) * 30;

            if (mouseX >= guiLeft + 10 && mouseX <= guiLeft + 26 && mouseY >= guiTop + yOffset && mouseY <= guiTop + yOffset + 16) {
                this.renderToolTip(recipe.getRecipe().getRecipeOutput(), mouseX, mouseY);
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
                updateRecipeButtons();
            }
        } else if (button == nextPageButton) {
            if ((currentPage + 1) * RECIPES_PER_PAGE < availableRecipes.size()) {
                currentPage++;
                updatePageButtons();
                updateRecipeButtons();
            }
        } else if (unlockButtons.contains(button)) {
            int index = button.id - 100 + currentPage * RECIPES_PER_PAGE;
            RecipeWrapper recipe = availableRecipes.get(index);
            if (progression.unlockRecipe(recipe)) {
                NetworkHandler.INSTANCE.sendToServer(new UnlockRecipeMessage(recipe.getId()));
                updateRecipeButtons();
            }
        } else if (forgetButtons.contains(button)) {
            int index = button.id - 200 + currentPage * RECIPES_PER_PAGE;
            RecipeWrapper recipe = availableRecipes.get(index);
            if (progression.forgetRecipe(recipe)) {
                NetworkHandler.INSTANCE.sendToServer(new UnlockRecipeMessage(recipe.getId(), true));
                updateRecipeButtons();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
