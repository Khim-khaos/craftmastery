package com.example.craftmastery;

import com.example.craftmastery.config.ModConfig;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.event.CraftingEventHandler;
import com.example.craftmastery.network.NetworkHandler;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION,
        acceptedMinecraftVersions = CraftMastery.MC_VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "Craft Mastery";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "[1.12.2]";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    @SidedProxy(clientSide = "com.example.craftmastery.proxy.ClientProxy",
            serverSide = "com.example.craftmastery.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        NetworkHandler.registerMessages();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new CraftingEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        CraftingManager.getInstance().initializeRecipes();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Регистрация серверных команд, если необходимо
    }

    public static CraftingManager getCraftingManager() {
        return CraftingManager.getInstance();
    }

    public static ProgressionManager getProgressionManager() {
        return ProgressionManager.getInstance();
    }
}
