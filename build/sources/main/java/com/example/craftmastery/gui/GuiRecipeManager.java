package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.recipe.CustomRecipe;
import com.example.craftmastery.recipe.RecipeCategory;
import com.example.craftmastery.recipe.RecipeManager;
import com.example.craftmastery.points.PlayerPoints;
import com.example.craftmastery.points.PointsManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class GuiRecipeManager extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/recipe_manager.png");
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;
    private static final int RECIPES_PER_PAGE = 5;

    private List<RecipeCategory> categories;
    private List<CustomRecipe> currentRecipes;
    private PlayerPoints playerPoints;
    private int currentCategory = 0;
    private int currentPage = 0;
    private int guiLeft;
    private int guiTop;

    public GuiRecipeManager(EntityPlayer player) {
        this.categories = RecipeManager.getCategories();
        this.playerPoints = PointsManager.get(player.world).getPlayerPoints(player);
        updateCurrentRecipes();
    }

    private void updateCurrentRecipes() {
        if (!categories.isEmpty()) {
            currentRecipes = categories.get(currentCategory).getRecipes();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (width - GUI_WIDTH) / 2;
        guiTop = (height - GUI_HEIGHT) / 2;

        // Кнопки для переключения категорий
        buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 5, 20, 20, "<"));
        buttonList.add(new GuiButton(1, guiLeft + 146, guiTop + 5, 20, 20, ">"));

        // Кнопки для переключения страниц
        buttonList.add(new GuiButton(2, guiLeft + 10, guiTop + 140, 20, 20, "<"));
        buttonList.add(new GuiButton(3, guiLeft + 146, guiTop + 140, 20, 20, ">"));

        // Кнопки для разблокировки рецептов
        for (int i = 0; i < RECIPES_PER_PAGE; i++) {
            buttonList.add(new GuiButton(4 + i, guiLeft + 120, guiTop + 30 + i * 20, 50, 20, "Unlock"));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        // Отрисовка названия текущей категории
        String categoryName = categories.isEmpty() ? "No Categories" : categories.get(currentCategory).getName();
        fontRenderer.drawString(categoryName, guiLeft + 88 - fontRenderer.getStringWidth(categoryName) / 2, guiTop + 10, 0x404040);

        // Отрисовка рецептов и их стоимости
        int recipeStart = currentPage * RECIPES_PER_PAGE;
        for (int i = 0; i < RECIPES_PER_PAGE && recipeStart + i < currentRecipes.size(); i++) {
            CustomRecipe recipe = currentRecipes.get(recipeStart + i);
            fontRenderer.drawString(recipe.getName(), guiLeft + 10, guiTop + 30 + i * 20, 0x404040);
            fontRenderer.drawString("Cost: " + recipe.getPointCost(), guiLeft + 80, guiTop + 30 + i * 20, 0x404040);

            // Отображаем кнопку "Unlock" только если рецепт не разблокирован
            GuiButton unlockButton = buttonList.get(4 + i);
            unlockButton.visible = !playerPoints.isRecipeUnlocked(recipe);
        }

        // Отображение текущих очков игрока
        fontRenderer.drawString("Points: " + playerPoints.getCraftPoints(), guiLeft + 10, guiTop + 155, 0x404040);

        // Отрисовка номера текущей страницы
        String pageInfo = String.format("Page %d/%d", currentPage + 1, (currentRecipes.size() - 1) / RECIPES_PER_PAGE + 1);
        fontRenderer.drawString(pageInfo, guiLeft + 88 - fontRenderer.getStringWidth(pageInfo) / 2, guiTop + 145, 0x404040);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: // Предыдущая категория
                currentCategory = (currentCategory - 1 + categories.size()) % categories.size();
                updateCurrentRecipes();
                currentPage = 0;
                break;
            case 1: // Следующая категория
                currentCategory = (currentCategory + 1) % categories.size();
                updateCurrentRecipes();
                currentPage = 0;
                break;
            case 2: // Предыдущая страница
                if (currentPage > 0) currentPage--;
                break;
            case 3: // Следующая страница
                if ((currentPage + 1) * RECIPES_PER_PAGE < currentRecipes.size()) currentPage++;
                break;
            default:
                if (button.id >= 4 && button.id < 4 + RECIPES_PER_PAGE) {
                    int recipeIndex = currentPage * RECIPES_PER_PAGE + (button.id - 4);
                    if (recipeIndex < currentRecipes.size()) {
                        CustomRecipe recipe = currentRecipes.get(recipeIndex);
                        if (playerPoints.unlockRecipe(recipe)) {
                            // Обновляем GUI после разблокировки рецепта
                            button.visible = false;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        // Здесь можно добавить код для сохранения изменений, если это необходимо
    }

    // Вспомогательный метод для отрисовки иконки предмета (если потребуется)
    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
