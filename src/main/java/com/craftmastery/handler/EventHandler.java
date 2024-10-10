package com.craftmastery.handler;

import com.craftmastery.CraftMastery;
import com.craftmastery.progression.ExperienceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (!player.world.isRemote) {
            ExperienceManager.getInstance().addExperience(player, 1); // Add 1 experience for breaking a block
        }
    }

    // Add more event handlers as needed, for example:
    /*
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            ExperienceManager.getInstance().addExperience(player, 2); // Add 2 experience for crafting an item
        }
    }
    */
}
