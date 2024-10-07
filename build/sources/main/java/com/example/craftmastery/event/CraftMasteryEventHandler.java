package com.example.craftmastery.event;

import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftMasteryEventHandler {

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack craftedItem = event.crafting;

        if (player.world.isRemote) {
            return;
        }

        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        CraftRecipe recipe = CraftingManager.getInstance().findRecipeByOutput(craftedItem);

        if (recipe != null && !playerData.isRecipeUnlocked(recipe) && !CraftRecipe.isItemAllowed(craftedItem)) {
            // Нельзя отменить крафт напрямую, поэтому мы удалим предмет из инвентаря игрока
            player.inventory.deleteStack(craftedItem);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.cannot_craft"));
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack pickedUpItem = event.getItem().getItem();

        if (player.world.isRemote) {
            return;
        }

        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        CraftRecipe recipe = CraftingManager.getInstance().findRecipeByOutput(pickedUpItem);

        if (recipe != null && !playerData.isRecipeUnlocked(recipe) && !CraftRecipe.isItemAllowed(pickedUpItem)) {
            event.setCanceled(true);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.cannot_use"));
        }
    }
}
