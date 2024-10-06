package com.example.craftmastery.command;

import com.example.craftmastery.points.PlayerPoints;
import com.example.craftmastery.points.PointsManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandGivePoints extends CommandBase {

    @Override
    public String getName() {
        return "craftmastery";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/craftmastery <give_points|give_reset_points> <player> <amount>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 3) {
            throw new CommandException("Invalid number of arguments");
        }

        String subCommand = args[0];
        EntityPlayerMP player = getPlayer(server, sender, args[1]);
        int amount = parseInt(args[2]);

        PointsManager pointsManager = PointsManager.get(player.world);
        PlayerPoints playerPoints = pointsManager.getPlayerPoints(player);

        switch (subCommand) {
            case "give_points":
                playerPoints.addCraftPoints(amount);
                sender.sendMessage(new TextComponentString("Given " + amount + " craft points to " + player.getName()));
                break;
            case "give_reset_points":
                playerPoints.addResetPoints(amount);
                sender.sendMessage(new TextComponentString("Given " + amount + " reset points to " + player.getName()));
                break;
            default:
                throw new CommandException("Unknown subcommand: " + subCommand);
        }

        pointsManager.markDirty();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
