package com.example.craftmastery.network;

import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.crafting.CraftingManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class MessageSyncRecipes implements IMessage {
    private List<CraftRecipe> recipes;

    public MessageSyncRecipes() {
        this.recipes = new ArrayList<>();
    }

    public MessageSyncRecipes(List<CraftRecipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(recipes.size());
        for (CraftRecipe recipe : recipes) {
            ByteBufUtils.writeUTF8String(buf, recipe.getId().toString());
            ByteBufUtils.writeItemStack(buf, recipe.getOutput());
            buf.writeInt(recipe.getIngredients().length);
            for (ItemStack ingredient : recipe.getIngredients()) {
                ByteBufUtils.writeItemStack(buf, ingredient);
            }
            buf.writeInt(recipe.getPointCost());
            ByteBufUtils.writeUTF8String(buf, recipe.getType());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int recipeCount = buf.readInt();
        recipes = new ArrayList<>(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            ResourceLocation id = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
            ItemStack output = ByteBufUtils.readItemStack(buf);
            int ingredientCount = buf.readInt();
            ItemStack[] ingredients = new ItemStack[ingredientCount];
            for (int j = 0; j < ingredientCount; j++) {
                ingredients[j] = ByteBufUtils.readItemStack(buf);
            }
            int pointCost = buf.readInt();
            String type = ByteBufUtils.readUTF8String(buf);
            recipes.add(new CraftRecipe(id, output, ingredients, pointCost, type));
        }
    }

    public static class Handler implements IMessageHandler<MessageSyncRecipes, IMessage> {
        @Override
        public IMessage onMessage(MessageSyncRecipes message, MessageContext ctx) {
            CraftingManager.getInstance().setRecipes(message.recipes);
            return null;
        }
    }
}
