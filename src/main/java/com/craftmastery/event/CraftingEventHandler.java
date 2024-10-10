package com.craftmastery.event;

import com.craftmastery.crafting.CraftManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingEventHandler {

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        String recipeId = event.crafting.getItem().getRegistryName().toString();

        if (!CraftManager.getInstance().isRecipeUnlocked(player, recipeId)) {
            event.setCanceled(true);
            // Можно добавить сообщение игроку о том, что крафт заблокирован
        }
    }
}
