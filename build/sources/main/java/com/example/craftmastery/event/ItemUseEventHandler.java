package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.progression.PlayerProgression;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class ItemUseEventHandler {

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack item = event.getItemStack();

        PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(player);

        if (!progression.canUseItem(item)) {
            event.setCanceled(true);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.item_locked"));
        }
    }
}
