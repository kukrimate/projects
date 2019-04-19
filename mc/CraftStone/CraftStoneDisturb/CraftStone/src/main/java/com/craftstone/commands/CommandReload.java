package com.craftstone.commands;

import com.craftstone.plugin.CraftPluginManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase {

	public CommandReload() {
		super("stone.reload", "You don't have permission to use this", "This command is for reloading plugins!");
	}
	
	@Override
	public String getCommandName() {
		return "reload";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/reload";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
		((CraftPluginManager)MinecraftServer.getServer().getPluginManager()).reloadPlugins();
		var1.sendMessage("Reload successfull!");
	}
	
	@Override
	   public boolean isVanillaCommand() { //CraftStone
		   return false;
	   }

}
