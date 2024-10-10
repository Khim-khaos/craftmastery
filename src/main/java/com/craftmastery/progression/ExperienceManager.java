package com.craftmastery.progression;

import com.craftmastery.player.PlayerDataManager;
import com.craftmastery.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExperienceManager {
    private static ExperienceManager instance;

    private ExperienceManager() {}

    public static ExperienceManager getInstance() {
        if (instance == null) {
            instance = new ExperienceManager();
        }
        return instance;
    }

    public void addExperience(EntityPlayer player, int amount) {
        PlayerDataManager.getInstance().getPlayerData(player).addExperience(amount);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        int expGain = ConfigHandler.getBaseExperiencePerAction();
        addExperience(player, expGain);
    }

    // Добавьте здесь другие методы для начисления опыта за различные действия
}
