package com.craftmastery;

import com.craftmastery.config.ConfigHandler;
import com.craftmastery.crafting.CraftManager;
import com.craftmastery.network.NetworkHandler;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.progression.LevelManager;
import com.craftmastery.proxy.CommonProxy;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "Craft Mastery";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    @SidedProxy(clientSide = "com.craftmastery.proxy.ClientProxy", serverSide = "com.craftmastery.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigHandler.init(event);
        proxy.preInit(event);
        NetworkHandler.registerMessages();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        CraftManager.getInstance().initializeBlockedRecipes();
        SpecializationManager.getInstance().initializeSpecializations();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Регистрация серверных команд, если они есть
    }

    public static void log(String message) {
        logger.info(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }
}
