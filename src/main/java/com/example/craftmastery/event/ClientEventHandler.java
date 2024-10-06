package com.example.craftmastery.event;

import com.example.craftmastery.gui.GuiRecipeManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class ClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_R) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                mc.displayGuiScreen(new GuiRecipeManager(mc.player));
            }
        }
    }
}
