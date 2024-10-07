package com.example.craftmastery.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // Клиентская предварительная инициализация
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        // Клиентская инициализация
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        // Клиентская пост-инициализация
    }
}
