package com.craftmastery.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_MAIN = 0;
    public static final int GUI_PLAYER_STATS = 1;
    public static final int GUI_RECIPE_BOOK = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_MAIN:
                return new GuiMain();
            case GUI_PLAYER_STATS:
                return new GuiPlayerStats();
            case GUI_RECIPE_BOOK:
                return new GuiRecipeBook();
            default:
                return null;
        }
    }
}
