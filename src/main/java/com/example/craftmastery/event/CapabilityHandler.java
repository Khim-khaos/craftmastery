package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.player.PlayerProgressProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class CapabilityHandler {

    public static final ResourceLocation PLAYER_PROGRESS = new ResourceLocation(CraftMastery.MODID, "player_progress");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(PLAYER_PROGRESS, new PlayerProgressProvider());
        }
    }
}
