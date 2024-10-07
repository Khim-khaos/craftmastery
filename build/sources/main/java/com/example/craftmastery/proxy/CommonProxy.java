package com.example.craftmastery.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        // Общая предварительная инициализация
    }

    public void init(FMLInitializationEvent event) {
        // Общая инициализация
    }

    public void postInit(FMLPostInitializationEvent event) {
        // Общая пост-инициализация
    }
}
