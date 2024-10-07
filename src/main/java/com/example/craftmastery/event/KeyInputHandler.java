package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.gui.GuiSkillTree;
import com.example.craftmastery.keybinding.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID, value = Side.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.openSkillTreeGui.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSkillTree(Minecraft.getMinecraft().player));
        }
    }
}
