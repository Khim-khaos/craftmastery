package com.craftmastery.gui;

import com.craftmastery.CraftMastery;
import com.craftmastery.specialization.Specialization;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class GuiSpecializationManager extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/specialization_manager.png");
    private List<Specialization> specializations;
    private int currentPage = 0;
    private static final int SPECIALIZATIONS_PER_PAGE = 5;

    public GuiSpecializationManager() {
        this.specializations = SpecializationManager.getInstance().getAllSpecializations();
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;

        // Add specialization buttons
        for (int index = 0; index < SPECIALIZATIONS_PER_PAGE; index++) {
            int specializationIndex = currentPage * SPECIALIZATIONS_PER_PAGE + index;
            if (specializationIndex < specializations.size()) {
                Specialization spec = specializations.get(specializationIndex);
                this.buttonList.add(new GuiButton(index, i + 10, j + 20 + index * 30, 150, 20, spec.getName()));
            }
        }

        // Add navigation buttons
        this.buttonList.add(new GuiButton(100, i + 10, j + 140, 20, 20, "<"));
        this.buttonList.add(new GuiButton(101, i + 218, j + 140, 20, 20, ">"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < SPECIALIZATIONS_PER_PAGE) {
            int specializationIndex = currentPage * SPECIALIZATIONS_PER_PAGE + button.id;
            if (specializationIndex < specializations.size()) {
                Specialization selectedSpec = specializations.get(specializationIndex);
                mc.displayGuiScreen(new GuiSpecializationDetails(selectedSpec));
            }
        } else if (button.id == 100) {
            if (currentPage > 0) {
                currentPage--;
                initGui();
            }
        } else if (button.id == 101) {
            if ((currentPage + 1) * SPECIALIZATIONS_PER_PAGE < specializations.size()) {
                currentPage++;
                initGui();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawString("Specialization Manager", i + 10, j + 10, 0x404040);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
