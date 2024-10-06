package com.example.craftmastery.commands;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CraftMasteryCommands extends CommandBase {

    private static final List<String> SUBCOMMANDS = Arrays.asList("setlevel", "addxp", "resetprogress", "addcraftpoints", "addresetpoints");

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
        if (args.length < 2) {
            throw new WrongUsageException(getUsage(sender));
        }

        String subCommand = args[0].toLowerCase();
        String playerName = args[1];
        EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(playerName);

        if (targetPlayer == null) {
            throw new CommandException("commands.craftmastery.playerNotFound", playerName);
        }

        PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(targetPlayer);

        switch (subCommand) {
            case "setlevel":
                if (args.length < 3) {
                    throw new WrongUsageException("commands.craftmastery.setlevel.usage");
                }
                int level = parseInt(args[2], 1);
                progression.setLevel(level);
                notifyCommandListener(sender, this, "commands.craftmastery.setlevel.success", playerName, level);
                break;

            case "addxp":
                if (args.length < 3) {
                    throw new WrongUsageException("commands.craftmastery.addxp.usage");
                }
                int xp = parseInt(args[2], 0);
                progression.addExperience(xp);
                notifyCommandListener(sender, this, "commands.craftmastery.addxp.success", xp, playerName);
                break;

            case "resetprogress":
                progression.resetProgress();
                notifyCommandListener(sender, this, "commands.craftmastery.resetprogress.success", playerName);
                break;

            case "addcraftpoints":
                if (args.length < 3) {
                    throw new WrongUsageException("commands.craftmastery.addcraftpoints.usage");
                }
                int craftPoints = parseInt(args[2], 0);
                progression.addCraftPoints(craftPoints);
                notifyCommandListener(sender, this, "commands.craftmastery.addcraftpoints.success", craftPoints, playerName);
                break;

            case "addresetpoints":
                if (args.length < 3) {
                    throw new WrongUsageException("commands.craftmastery.addresetpoints.usage");
                }
                int resetPoints = parseInt(args[2], 0);
                progression.addResetPoints(resetPoints);
                notifyCommandListener(sender, this, "commands.craftmastery.addresetpoints.success", resetPoints, playerName);
                break;

            default:
                throw new WrongUsageException(getUsage(sender));
        }

        // Синхронизация прогресса с клиентом
        ProgressionManager.getInstance().syncProgressionToClient(targetPlayer);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SUBCOMMANDS);
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Уровень разрешения для операторов сервера
    }
}
