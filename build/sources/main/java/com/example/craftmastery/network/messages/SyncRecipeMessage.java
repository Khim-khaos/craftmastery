package com.example.craftmastery.network.messages;

import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncRecipeMessage implements IMessage {
    private String recipeId;
    private int pointCost;
    private String categoryId;

    public SyncRecipeMessage() {}

    public SyncRecipeMessage(RecipeWrapper recipe) {
        this.recipeId = recipe.getId();
        this.pointCost = recipe.getPointCost();
        this.categoryId = recipe.getCategoryId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, recipeId);
        buf.writeInt(pointCost);
        ByteBufUtils.writeUTF8String(buf, categoryId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        recipeId = ByteBufUtils.readUTF8String(buf);
        pointCost = buf.readInt();
        categoryId = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<SyncRecipeMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncRecipeMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                CraftingManager.getInstance().addRecipe(message.recipeId, null, message.pointCost, message.categoryId);
            });
            return null;
        }
    }
}
