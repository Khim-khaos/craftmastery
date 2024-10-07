package com.example.craftmastery;

import com.example.craftmastery.keybinding.KeyBindings;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "Craft Mastery";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Предварительная инициализация
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Инициализация
        if (event.getSide().isClient()) {
            KeyBindings.init();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Пост-инициализация
    }
}
