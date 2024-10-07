package com.example.craftmastery;

import com.example.craftmastery.commands.CommandCraftMastery;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "CraftMastery";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Инициализация конфигурации, регистрация предметов и блоков
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Регистрация обработчиков событий, рецептов и т.д.
    }
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandCraftMastery());
    }
}
