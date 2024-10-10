package com.craftmastery.event;

import com.craftmastery.config.CraftMasteryConfig;
import com.craftmastery.player.PlayerData;
import com.craftmastery.player.PlayerDataManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ExperienceEventHandler {

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(event.player);
        int xpGained = CraftMasteryConfig.EXPERIENCE.baseCraftingXP;
        playerData.addExperience(xpGained);
    }

    @SubscribeEvent
    public void onBlockHarvested(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() != null) {
            PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(event.getHarvester());
            int xpGained = CraftMasteryConfig.EXPERIENCE.baseMiningXP;
            playerData.addExperience(xpGained);
        }
    }
}
