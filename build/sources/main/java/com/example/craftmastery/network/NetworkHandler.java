package com.example.craftmastery.network;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.network.messages.UnlockRecipeMessage;
import com.example.craftmastery.network.messages.UnlockUpgradeMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CraftMastery.MODID);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(UnlockRecipeMessage.Handler.class, UnlockRecipeMessage.class, id++, Side.SERVER);
        INSTANCE.registerMessage(UnlockUpgradeMessage.Handler.class, UnlockUpgradeMessage.class, id++, Side.SERVER);
    }
}
