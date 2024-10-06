package com.example.craftmastery.network.messages;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlockUpgradeMessage implements IMessage {

    private String upgradeId;

    public UnlockUpgradeMessage() {
        // Пустой конструктор необходим для десериализации
    }

    public UnlockUpgradeMessage(String upgradeId) {
        this.upgradeId = upgradeId;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, upgradeId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        upgradeId = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<UnlockUpgradeMessage, IMessage> {
        @Override
        public IMessage onMessage(UnlockUpgradeMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(player);

                // Здесь вы должны получить стоимость улучшения из вашей системы улучшений
                int upgradeCost = getUpgradeCost(message.upgradeId);

                if (progression.unlockUpgrade(message.upgradeId, upgradeCost)) {
                    CraftMastery.logger.info("Player {} unlocked upgrade: {}", player.getName(), message.upgradeId);
                    // Отправьте обновление клиенту, если необходимо
                    ProgressionManager.getInstance().syncProgressionToClient(player);
                } else {
                    CraftMastery.logger.warn("Player {} failed to unlock upgrade: {}", player.getName(), message.upgradeId);
                    // Возможно, здесь стоит отправить сообщение игроку о неудаче
                }
            });
            return null;
        }

        private int getUpgradeCost(String upgradeId) {
            // Здесь должна быть логика получения стоимости улучшения
            // Например, из конфигурации или из системы управления улучшениями
            return 10; // Пример фиксированной стоимости
        }
    }
}
