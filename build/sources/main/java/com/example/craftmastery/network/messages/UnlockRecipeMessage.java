package com.example.craftmastery.network.messages;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlockRecipeMessage implements IMessage {

    private String recipeId;
    private boolean isForget;

    public UnlockRecipeMessage() {
        // Пустой конструктор необходим для десериализации
    }

    public UnlockRecipeMessage(String recipeId) {
        this(recipeId, false);
    }

    public UnlockRecipeMessage(String recipeId, boolean isForget) {
        this.recipeId = recipeId;
        this.isForget = isForget;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, recipeId);
        buf.writeBoolean(isForget);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        recipeId = ByteBufUtils.readUTF8String(buf);
        isForget = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<UnlockRecipeMessage, IMessage> {
        @Override
        public IMessage onMessage(UnlockRecipeMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(player);
                RecipeWrapper recipe = CraftingManager.getInstance().getRecipe(message.recipeId);

                if (recipe != null) {
                    if (message.isForget) {
                        if (progression.forgetRecipe(recipe)) {
                            CraftMastery.logger.info("Player {} forgot recipe: {}", player.getName(), message.recipeId);
                            ProgressionManager.getInstance().syncProgressionToClient(player);
                        } else {
                            CraftMastery.logger.warn("Player {} failed to forget recipe: {}", player.getName(), message.recipeId);
                        }
                    } else {
                        if (progression.unlockRecipe(recipe)) {
                            CraftMastery.logger.info("Player {} unlocked recipe: {}", player.getName(), message.recipeId);
                            ProgressionManager.getInstance().syncProgressionToClient(player);
                        } else {
                            CraftMastery.logger.warn("Player {} failed to unlock recipe: {}", player.getName(), message.recipeId);
                        }
                    }
                } else {
                    CraftMastery.logger.error("Recipe not found: {}", message.recipeId);
                }
            });
            return null;
        }
    }
}
