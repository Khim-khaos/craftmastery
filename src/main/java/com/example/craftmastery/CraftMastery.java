package com.example.craftmastery;

import com.example.craftmastery.proxy.CommonProxy;
import com.example.craftmastery.recipe.RecipeManager;
import com.example.craftmastery.player.PlayerProgressProvider;
import com.example.craftmastery.network.PacketHandler;
import com.example.craftmastery.event.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "Craft Mastery";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.example.craftmastery.proxy.ClientProxy", serverSide = "com.example.craftmastery.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("CraftMastery pre-initialization");

        // Register capabilities
        PlayerProgressProvider.register();

        // Initialize network handler
        PacketHandler.registerMessages(MODID);

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("CraftMastery initialization");

        // Initialize recipes
        RecipeManager.initializeRecipes();

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("CraftMastery post-initialization");
        proxy.postInit(event);
    }
}
