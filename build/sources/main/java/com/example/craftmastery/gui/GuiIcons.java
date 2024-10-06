package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiIcons {
    private static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/icons.png");

    public static void drawCraftPointIcon(Minecraft mc, int x, int y) {
        mc.getTextureManager().bindTexture(ICONS_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, 16, 16);
    }

    public static void drawUncraftPointIcon(Minecraft mc, int x, int y) {
        mc.getTextureManager().bindTexture(ICONS_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.ingameGUI.drawTexturedModalRect(x, y, 16, 0, 16, 16);
    }
}
