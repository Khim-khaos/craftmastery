package com.example.craftmastery.commands;

import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
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
        if (args.length < 2) {
            throw new CommandException("commands.craftmastery.usage");
        }

        String subCommand = args[0];
        EntityPlayerMP player = getPlayer(server, sender, args[1]);
        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        switch (subCommand) {
            case "unlock_all":
                for (ResourceLocation recipeId : CraftingManager.getInstance().getAllRecipeIds()) {
                    CraftRecipe recipe = CraftingManager.getInstance().getRecipeById(recipeId);
                    playerData.unlockRecipe(recipe);
                }
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.unlock_all.success", player.getName()));
                break;
            case "lock_all":
                playerData.lockAllRecipes();
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.lock_all.success", player.getName()));
                break;
            case "unlock_recipe":
                if (args.length < 3) {
                    throw new CommandException("commands.craftmastery.unlock_recipe.usage");
                }
                ResourceLocation recipeId = new ResourceLocation(args[2]);
                CraftRecipe recipe = CraftingManager.getInstance().getRecipeById(recipeId);
                if (recipe != null) {
                    playerData.unlockRecipe(recipe);
                    sender.sendMessage(new TextComponentTranslation("commands.craftmastery.unlock_recipe.success", args[2], player.getName()));
                } else {
                    throw new CommandException("commands.craftmastery.unlock_recipe.not_found", args[2]);
                }
                break;
            case "give_points":
                if (args.length < 3) {
                    throw new CommandException("commands.craftmastery.give_points.usage");
                }
                int points = parseInt(args[2]);
                playerData.addPoints(points);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_points.success", points, player.getName()));
                break;
            case "give_reset_points":
                if (args.length < 3) {
                    throw new CommandException("commands.craftmastery.give_reset_points.usage");
                }
                int resetPoints = parseInt(args[2]);
                playerData.addResetPoints(resetPoints);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_reset_points.success", resetPoints, player.getName()));
                break;
            case "give_undo_points":
                if (args.length < 3) {
                    throw new CommandException("commands.craftmastery.give_undo_points.usage");
                }
                int undoPoints = parseInt(args[2]);
                playerData.addUndoPoints(undoPoints);
                sender.sendMessage(new TextComponentTranslation("commands.craftmastery.give_undo_points.success", undoPoints, player.getName()));
                break;
            default:
                throw new CommandException("commands.craftmastery.unknown_subcommand");
        }

        PlayerDataManager.savePlayerData(player);
    }
}
