package com.example.craftmastery.network;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SyncHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CraftMastery.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessageSyncRecipes.Handler.class, MessageSyncRecipes.class, 0, Side.CLIENT);
    }

    public static void sendRecipesToClient(EntityPlayerMP player) {
        INSTANCE.sendTo(new MessageSyncRecipes(CraftingManager.getInstance().getAllRecipes()), player);
    }
}
