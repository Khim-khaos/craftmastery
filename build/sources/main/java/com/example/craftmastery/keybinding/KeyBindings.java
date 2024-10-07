package com.example.craftmastery.keybinding;

import com.example.craftmastery.CraftMastery;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding openSkillTreeKey;

    @SideOnly(Side.CLIENT)
    public static void init() {
        openSkillTreeKey = new KeyBinding("key.openskillgui.desc", Keyboard.KEY_K, "key.categories." + CraftMastery.MODID);
        ClientRegistry.registerKeyBinding(openSkillTreeKey);
    }
}
