package com.example.craftmastery.network;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.network.messages.SyncRecipeMessage;
import com.example.craftmastery.network.messages.SyncProgressionMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CraftMastery.MODID);

    private static int id = 0;

    public static void registerMessages() {
        INSTANCE.registerMessage(SyncRecipeMessage.Handler.class, SyncRecipeMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(SyncProgressionMessage.Handler.class, SyncProgressionMessage.class, id++, Side.CLIENT);
    }
}
