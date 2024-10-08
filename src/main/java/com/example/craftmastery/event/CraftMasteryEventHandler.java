package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class CraftMasteryEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            playerData = new PlayerData();
            PlayerDataManager.setPlayerData(player, playerData);
        }
        CraftMastery.logger.info("Player " + player.getName() + " logged in. Loaded player data.");
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        PlayerDataManager.savePlayerData(player);
        PlayerDataManager.removePlayerData(player);
        CraftMastery.logger.info("Player " + player.getName() + " logged out. Saved and removed player data.");
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerData oldData = PlayerDataManager.getPlayerData(event.getOriginal());
            PlayerData newData = new PlayerData();
            newData.deserializeNBT(oldData.serializeNBT());
            PlayerDataManager.setPlayerData(event.getEntityPlayer(), newData);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (!player.world.isRemote) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            int pointsToAdd = 1; // Можно настроить количество очков в зависимости от блока
            playerData.addPoints(pointsToAdd);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.points_gained", pointsToAdd));
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            int pointsToAdd = 2; // Можно настроить количество очков в зависимости от предмета
            playerData.addPoints(pointsToAdd);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.points_gained", pointsToAdd));
        }
    }
}
