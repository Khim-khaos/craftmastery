package com.craftmastery.command;

import com.craftmastery.item.ItemRecipeBook;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandCreateRecipeBook extends CommandBase {
    @Override
    public String getName() {
        return "createrecipebook";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/createrecipebook <type>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) {
            throw new CommandException("This command can only be used by players");
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (!player.canUseCommand(2, "createrecipebook")) {
            throw new CommandException("You don't have permission to use this command");
        }

        if (args.length < 1) {
            throw new CommandException("Please specify a recipe book type");
        }

        String type = args[0].toLowerCase();
        ItemStack heldItem = player.getHeldItemMainhand();

        if (heldItem.isEmpty()) {
            throw new CommandException("You must be holding an item to convert into a recipe book");
        }

        ItemStack recipeBook = new ItemStack(new ItemRecipeBook(type));
        player.setHeldItem(player.getActiveHand(), recipeBook);
        player.sendMessage(new TextComponentString("Created a " + type + " recipe book"));
    }
}
