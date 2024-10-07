package com.example.craftmastery.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding openGuiKey;

    public static void init() {
        openGuiKey = new KeyBinding("key.craftmastery.opengui", Keyboard.KEY_M, "key.categories.craftmastery");
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
}
