package com.craftmastery.handler;

import com.craftmastery.CraftMastery;
import com.craftmastery.gui.GuiHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public static KeyBinding openGuiKey;

    public static void registerKeyBindings() {
        openGuiKey = new KeyBinding("key.opengui.desc", Keyboard.KEY_G, "key.craftmastery.category");
        ClientRegistry.registerKeyBinding(openGuiKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openGuiKey.isPressed()) {
            CraftMastery.proxy.openGui(GuiHandler.GUI_MAIN);
        }
    }
}
