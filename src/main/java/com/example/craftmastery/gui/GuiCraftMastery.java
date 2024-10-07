package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiCraftMastery extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/craftmastery.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;

    private List<String> categories;
    private int currentCategory = 0;
    private List<CraftRecipe> visibleRecipes = new ArrayList<>();
    private PlayerData playerData;

    private GuiButton prevCategoryButton;
    private GuiButton nextCategoryButton;
    private GuiButton filterBasicButton;
    private GuiButton filterTechnicalButton;
    private GuiButton filterMagicalButton;
    private GuiButton addCategoryButton;

    private String currentFilter = "all";

    public GuiCraftMastery() {
        this.categories = CraftingManager.getInstance().getCategories();
        this.playerData = PlayerDataManager.getPlayerData(mc.player);
    }

    @Override
    public void initGui() {
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        buttonList.clear();
        prevCategoryButton = addButton(new GuiButton(0, guiLeft + 10, guiTop + 5, 20, 20, "<"));
        nextCategoryButton = addButton(new GuiButton(1, guiLeft + GUI_WIDTH - 30, guiTop + 5, 20, 20, ">"));

        filterBasicButton = addButton(new GuiButton(2, guiLeft + 10, guiTop + GUI_HEIGHT + 5, 60, 20, I18n.format("gui.craftmastery.filter.basic")));
        filterTechnicalButton = addButton(new GuiButton(3, guiLeft + 75, guiTop + GUI_HEIGHT + 5, 60, 20, I18n.format("gui.craftmastery.filter.technical")));
        filterMagicalButton = addButton(new GuiButton(4, guiLeft + 140, guiTop + GUI_HEIGHT + 5, 60, 20, I18n.format("gui.craftmastery.filter.magical")));
        addCategoryButton = addButton(new GuiButton(5, guiLeft + GUI_WIDTH - 70, guiTop + GUI_HEIGHT + 5, 60, 20, I18n.format("gui.craftmastery.add_category")));

        updateVisibleRecipes();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String categoryName = categories.isEmpty() ? I18n.format("gui.craftmastery.no_categories") : categories.get(currentCategory);
        fontRenderer.drawString(categoryName, guiLeft + GUI_WIDTH / 2 - fontRenderer.getStringWidth(categoryName) / 2, guiTop + 10, 0x404040);

        drawRecipes(guiLeft, guiTop, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);

        drawHoveringText(mouseX, mouseY);
    }

    private void drawRecipes(int guiLeft, int guiTop, int mouseX, int mouseY) {
        int recipeStartX = guiLeft + 10;
        int recipeStartY = guiTop + 30;
        int recipeSize = 32;
        int recipesPerRow = 7;

        for (int i = 0; i < visibleRecipes.size(); i++) {
            CraftRecipe recipe = visibleRecipes.get(i);
            int x = recipeStartX + (i % recipesPerRow) * recipeSize;
            int y = recipeStartY + (i / recipesPerRow) * recipeSize;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);

            if (playerData.isRecipeUnlocked(recipe) || CraftRecipe.isItemAllowed(recipe.getOutput())) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(recipe.getOutput(), 0, 0);
            } else {
                mc.getTextureManager().bindTexture(TEXTURE);
                drawTexturedModalRect(0, 0, 0, 200, 16, 16);
            }

            GlStateManager.popMatrix();

            if (mouseX >= x && mouseX < x + 32 && mouseY >= y && mouseY < y + 32) {
                drawRect(x, y, x + 32, y + 32, 0x80FFFFFF);
            }
        }
    }

    private void drawHoveringText(int mouseX, int mouseY) {
        int recipeStartX = (this.width - GUI_WIDTH) / 2 + 10;
        int recipeStartY = (this.height - GUI_HEIGHT) / 2 + 30;
        int recipeSize = 32;
        int recipesPerRow = 7;

        for (int i = 0; i < visibleRecipes.size(); i++) {
            int x = recipeStartX + (i % recipesPerRow) * recipeSize;
            int y = recipeStartY + (i / recipesPerRow) * recipeSize;

            if (mouseX >= x && mouseX < x + 32 && mouseY >= y && mouseY < y + 32) {
                CraftRecipe recipe = visibleRecipes.get(i);
                List<String> tooltip = new ArrayList<>();

                tooltip.add(recipe.getOutput().getDisplayName());

                if (!playerData.isRecipeUnlocked(recipe) && !CraftRecipe.isItemAllowed(recipe.getOutput())) {
                    tooltip.add(TextFormatting.RED + I18n.format("gui.craftmastery.locked"));
                    tooltip.add(TextFormatting.GRAY + I18n.format("gui.craftmastery.cost", recipe.getPointCost()));

                    if (!recipe.getDependencies().isEmpty()) {
                        tooltip.add(TextFormatting.YELLOW + I18n.format("gui.craftmastery.dependencies"));
                        for (CraftRecipe dependency : recipe.getDependencies()) {
                            String dependencyName = dependency.getOutput().getDisplayName();
                            String unlockStatus = playerData.isRecipeUnlocked(dependency) ? TextFormatting.GREEN + "✓" : TextFormatting.RED + "✗";
                            tooltip.add(TextFormatting.GRAY + " - " + dependencyName + " " + unlockStatus);
                        }
                    }
                } else {
                    tooltip.add(TextFormatting.GREEN + I18n.format("gui.craftmastery.unlocked"));
                }

                this.drawHoveringText(tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == prevCategoryButton) {
            currentCategory = (currentCategory - 1 + categories.size()) % categories.size();
        } else if (button == nextCategoryButton) {
            currentCategory = (currentCategory + 1) % categories.size();
        } else if (button == filterBasicButton) {
            currentFilter = "basic";
        } else if (button == filterTechnicalButton) {
            currentFilter = "technical";
        } else if (button == filterMagicalButton) {
            currentFilter = "magical";
        } else if (button == addCategoryButton) {
            mc.displayGuiScreen(new GuiAddCategory(this));
        }
        updateVisibleRecipes();
    }

    private void updateVisibleRecipes() {
        if (!categories.isEmpty()) {
            List<CraftRecipe> allRecipes = CraftingManager.getInstance().getRecipes(categories.get(currentCategory));
            visibleRecipes = allRecipes.stream()
                    .filter(recipe -> currentFilter.equals("all") || recipe.getType().equals(currentFilter))
                    .collect(Collectors.toList());
        } else {
            visibleRecipes.clear();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int recipeStartX = (this.width - GUI_WIDTH) / 2 + 10;
        int recipeStartY = (this.height - GUI_HEIGHT) / 2 + 30;
        int recipeSize = 32;
        int recipesPerRow = 7;

        for (int i = 0; i < visibleRecipes.size(); i++) {
            int x = recipeStartX + (i % recipesPerRow) * recipeSize;
            int y = recipeStartY + (i / recipesPerRow) * recipeSize;

            if (mouseX >= x && mouseX < x + recipeSize && mouseY >= y && mouseY < y + recipeSize) {
                CraftRecipe recipe = visibleRecipes.get(i);
                if (!playerData.isRecipeUnlocked(recipe) && !CraftRecipe.isItemAllowed(recipe.getOutput())) {
                    if (playerData.unlockRecipe(recipe)) {
                        mc.player.sendMessage(new TextComponentTranslation("message.craftmastery.recipe_unlocked", recipe.getOutput().getDisplayName()));
                    } else {
                        mc.player.sendMessage(new TextComponentTranslation("message.craftmastery.not_enough_points"));
                    }
                }
                break;
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        prevCategoryButton.enabled = categories.size() > 1;
        nextCategoryButton.enabled = categories.size() > 1;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        PlayerDataManager.savePlayerData(mc.player);
    }

    public void addCategory(String categoryName) {
        CraftingManager.getInstance().addCategory(categoryName);
        categories = CraftingManager.getInstance().getCategories();
        updateVisibleRecipes();
    }
}