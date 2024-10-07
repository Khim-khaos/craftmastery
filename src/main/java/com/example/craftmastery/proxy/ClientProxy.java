package com.example.craftmastery.proxy;

import com.example.craftmastery.client.KeyBindings;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        KeyBindings.init();
    }
}
