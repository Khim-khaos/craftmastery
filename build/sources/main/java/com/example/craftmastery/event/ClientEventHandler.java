package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID, value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // Здесь можно добавить любую логику, которая должна выполняться каждый клиентский тик
            // Например, обновление пользовательского интерфейса или проверка определенных условий
        }
    }

    // Можно добавить другие обработчики клиентских событий по мере необходимости
    // Например:
    /*
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        // Рендеринг пользовательских элементов в мире
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelBake(ModelBakeEvent event) {
        // Обработка пользовательских моделей
    }
    */
}
