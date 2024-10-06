package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int PROGRESSION_GUI = 0;
    public static final int UPGRADES_GUI = 1;
    public static final int RECIPES_GUI = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case PROGRESSION_GUI:
                return new ProgressionGui(player);
            case UPGRADES_GUI:
                return new UpgradesGui(player);
            case RECIPES_GUI:
                return new RecipesGui(player);
            default:
                return null;
        }
    }

    public static void registerGuiHandler() {
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler(CraftMastery.instance, new GuiHandler());
    }
}
