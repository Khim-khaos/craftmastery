package com.example.craftmastery.commands;

import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandCraftMastery extends CommandBase {

    @Override
    public String getName() {
        return "craftmastery";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.craftmastery.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("commands.craftmastery.usage");
        }

        String subCommand = args[0];
        EntityPlayerMP player = getPlayer(server, sender, args[1]);
        int amount = parseInt(args[2]);

        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        switch (subCommand) {
            case "give_points":
                playerData.addPoints(amount);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_points.success", amount, player.getName()));
                break;
            case "give_reset_points":
                playerData.addResetPoints(amount);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_reset_points.success", amount, player.getName()));
                break;
            case "give_undo_points":
                playerData.addUndoPoints(amount);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_undo_points.success", amount, player.getName()));
                break;
            default:
                throw new CommandException("commands.craftmastery.unknown_subcommand");
        }

        PlayerDataManager.savePlayerData(player);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Уровень прав оператора
    }
}
