package com.craftmastery.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import com.craftmastery.CraftMastery;

public class GuiCraftMastery extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/craftmastery.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);

        // Отрисовка элементов интерфейса
        drawCategories();
        drawCraftTypes();
        drawAdminControls();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCategories() {
        // Отрисовка категорий
    }

    private void drawCraftTypes() {
        // Отрисовка типов крафтов
    }

    private void drawAdminControls() {
        // Отрисовка элементов управления для администраторов
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Обработка кликов мыши
    }
}
