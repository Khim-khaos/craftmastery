package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiUtils {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/elements.png");

    public static void drawCustomButton(Minecraft mc, int x, int y, int width, int height, String text, boolean hovered) {
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int textureY = hovered ? 20 : 0;
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, textureY, width / 2, height, 256, 256);
        Gui.drawModalRectWithCustomSizedTexture(x + width / 2, y, 200 - width / 2, textureY, width / 2, height, 256, 256);

        int textColor = hovered ? 0xFFFFFF : 0xE0E0E0;
        mc.fontRenderer.drawStringWithShadow(text, x + (width - mc.fontRenderer.getStringWidth(text)) / 2, y + (height - 8) / 2, textColor);
    }

    public static void drawCustomPanel(Minecraft mc, int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw corners
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 40, 4, 4, 256, 256);
        Gui.drawModalRectWithCustomSizedTexture(x + width - 4, y, 252, 40, 4, 4, 256, 256);
        Gui.drawModalRectWithCustomSizedTexture(x, y + height - 4, 0, 252, 4, 4, 256, 256);
        Gui.drawModalRectWithCustomSizedTexture(x + width - 4, y + height - 4, 252, 252, 4, 4, 256, 256);

        // Draw edges
        for (int i = 4; i < width - 4; i += 4) {
            Gui.drawModalRectWithCustomSizedTexture(x + i, y, 4, 40, Math.min(4, width - 4 - i), 4, 256, 256);
            Gui.drawModalRectWithCustomSizedTexture(x + i, y + height - 4, 4, 252, Math.min(4, width - 4 - i), 4, 256, 256);
        }
        for (int j = 4; j < height - 4; j += 4) {
            Gui.drawModalRectWithCustomSizedTexture(x, y + j, 0, 44, 4, Math.min(4, height - 4 - j), 256, 256);
            Gui.drawModalRectWithCustomSizedTexture(x + width - 4, y + j, 252, 44, 4, Math.min(4, height - 4 - j), 256, 256);
        }

        // Draw center
        for (int i = 4; i < width - 4; i += 4) {
            for (int j = 4; j < height - 4; j += 4) {
                Gui.drawModalRectWithCustomSizedTexture(x + i, y + j, 4, 44, Math.min(4, width - 4 - i), Math.min(4, height - 4 - j), 256, 256);
            }
        }
    }
}
