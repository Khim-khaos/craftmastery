package com.craftmastery;

import com.craftmastery.crafting.CraftManager;
import com.craftmastery.gui.GuiHandler;
import com.craftmastery.handler.KeyHandler;
import com.craftmastery.network.PacketHandler;
import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.proxy.CommonProxy;
import com.craftmastery.specialization.SpecializationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CraftMastery.MODID, name = CraftMastery.NAME, version = CraftMastery.VERSION)
public class CraftMastery {
    public static final String MODID = "craftmastery";
    public static final String NAME = "Craft Mastery";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static CraftMastery instance;

    @SidedProxy(clientSide = "com.craftmastery.proxy.ClientProxy", serverSide = "com.craftmastery.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("CraftMastery PreInit started");

        // Initialize PacketHandler
        PacketHandler.registerMessages(MODID);

        // Initialize Managers
        CraftManager.getInstance().initializeRecipes();
        SpecializationManager.getInstance().initializeSpecializations();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("CraftMastery Init started");

        // Register GUI Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        // Register Key Bindings
        KeyHandler.registerKeyBindings();

        // Register Event Handlers
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        MinecraftForge.EVENT_BUS.register(PlayerDataManager.getInstance());

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("CraftMastery PostInit started");
        proxy.postInit(event);
    }
}
