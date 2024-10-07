package com.example.craftmastery.client;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.gui.GuiCraftMastery;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID, value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.openGuiKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiCraftMastery());
        }
    }
}
