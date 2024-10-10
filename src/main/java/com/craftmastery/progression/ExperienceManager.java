package com.craftmastery.progression;

import com.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExperienceManager {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        // Add experience for breaking blocks
        addExperience(player, 1);
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        EntityPlayer player = event.getPlayer();
        // Add experience for placing blocks
        addExperience(player, 1);
    }

    public void onItemCrafted(EntityPlayer player, ItemStack crafted) {
        // Add experience for crafting items
        addExperience(player, crafted.getCount());
    }

    private void addExperience(EntityPlayer player, int amount) {
        PlayerDataManager.getInstance().getPlayerData(player).addExperience(amount);
    }
}
