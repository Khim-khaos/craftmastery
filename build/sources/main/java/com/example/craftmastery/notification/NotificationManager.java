package com.example.craftmastery.notification;

import com.example.craftmastery.CraftMastery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public class NotificationManager {
    private static final ResourceLocation NOTIFICATION_TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/notification.png");
    private static final int NOTIFICATION_WIDTH = 200;
    private static final int NOTIFICATION_HEIGHT = 20;
    private static final int DISPLAY_TIME = 200; // Ticks

    private static final List<Notification> activeNotifications = new ArrayList<>();

    public static void addNotification(String message) {
        activeNotifications.add(new Notification(message));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Проверяем, существует ли текстура
        if (mc.getTextureManager().getTexture(NOTIFICATION_TEXTURE) == null) {
            CraftMastery.logger.error("Notification texture not found: " + NOTIFICATION_TEXTURE);
            GlStateManager.popMatrix();
            return;
        }

        mc.getTextureManager().bindTexture(NOTIFICATION_TEXTURE);

        Iterator<Notification> iterator = activeNotifications.iterator();
        int yOffset = 10;
        while (iterator.hasNext()) {
            Notification notification = iterator.next();
            if (notification.render(mc, width, height, yOffset)) {
                yOffset += NOTIFICATION_HEIGHT + 5;
            } else {
                iterator.remove();
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static class Notification {
        private final String message;
        private int ticksLeft;

        public Notification(String message) {
            this.message = message;
            this.ticksLeft = DISPLAY_TIME;
        }

        public boolean render(Minecraft mc, int screenWidth, int screenHeight, int yOffset) {
            if (ticksLeft <= 0) {
                return false;
            }

            int x = (screenWidth - NOTIFICATION_WIDTH) / 2;
            int y = screenHeight - NOTIFICATION_HEIGHT - yOffset;

            GuiIngame gui = mc.ingameGUI;
            gui.drawTexturedModalRect(x, y, 0, 0, NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
            gui.drawCenteredString(mc.fontRenderer, message, x + NOTIFICATION_WIDTH / 2, y + 6, 0xFFFFFF);

            ticksLeft--;
            return true;
        }
    }
}
