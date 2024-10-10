package com.craftmastery;

import com.craftmastery.command.CommandCreateRecipeBook;
import com.craftmastery.config.ConfigHandler;
import com.craftmastery.crafting.CraftManager;
import com.craftmastery.gui.GuiHandler;
import com.craftmastery.handler.KeyHandler;
import com.craftmastery.item.ModItems;
import com.craftmastery.network.NetworkHandler;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.proxy.CommonProxy;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
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

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Pre-initialization started");
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        proxy.preInit(event);
        NetworkHandler.registerMessages();
        ModItems.init();

        if (event.getSide().isClient()) {
            KeyHandler.registerKeyBindings();
        }
        LOGGER.info("Pre-initialization completed");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Initialization started");
        proxy.init(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        CraftManager.getInstance().initializeRecipes();
        SpecializationManager.getInstance().initializeSpecializations();

        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new KeyHandler());
        }
        LOGGER.info("Initialization completed");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Post-initialization started");
        proxy.postInit(event);
        LOGGER.info("Post-initialization completed");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        LOGGER.info("Server starting, registering commands");
        event.registerServerCommand(new CommandCreateRecipeBook());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        LOGGER.info("Server stopping, saving player data");
        PlayerDataManager.getInstance().saveAllPlayerData();
    }
}
