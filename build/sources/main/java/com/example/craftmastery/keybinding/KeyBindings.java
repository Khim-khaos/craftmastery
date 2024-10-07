package com.example.craftmastery.keybinding;

import com.example.craftmastery.CraftMastery;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding openSkillTreeGui;

    public static void init() {
        openSkillTreeGui = new KeyBinding("key.openskillgui.desc", Keyboard.KEY_K, "key.categories." + CraftMastery.MODID);
        ClientRegistry.registerKeyBinding(openSkillTreeGui);
    }
}
