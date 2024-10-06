package com.example.craftmastery.network.messages;

import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.progression.PlayerProgression;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SyncProgressionMessage implements IMessage {
    private UUID playerId;
    private Set<String> unlockedRecipes;

    public SyncProgressionMessage() {}

    public SyncProgressionMessage(PlayerProgression progression) {
        this.playerId = progression.getPlayerId();
        this.unlockedRecipes = progression.getUnlockedRecipes();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerId.toString());
        buf.writeInt(unlockedRecipes.size());
        for (String recipeId : unlockedRecipes) {
            ByteBufUtils.writeUTF8String(buf, recipeId);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        int size = buf.readInt();
        unlockedRecipes = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            unlockedRecipes.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    public static class Handler implements IMessageHandler<SyncProgressionMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncProgressionMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(message.playerId);
                progression.setUnlockedRecipes(message.unlockedRecipes);
            });
            return null;
        }
    }
}
