package com.example.craftmastery.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import com.example.craftmastery.CraftMastery;

public class SyncHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CraftMastery.MODID);

    public static void init() {
        // Регистрация пакетов для синхронизации
        INSTANCE.registerMessage(MessageSyncRecipes.Handler.class, MessageSyncRecipes.class, 0, Side.CLIENT);
    }
}
