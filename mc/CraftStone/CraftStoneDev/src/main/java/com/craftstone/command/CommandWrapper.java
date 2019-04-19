package com.craftstone.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.craftstone.stone.command.Command;
import com.craftstone.stone.command.CommandExecuter;

public class CommandWrapper extends CommandBase implements Command {

	String name;
	String[] aliases;
	String usage;
	String permission;
	String permissionMessage;
	List<CommandExecuter> executer;
	
	public CommandWrapper(String name, String[] aliases, String usage, String description, String permission, String permissionMessage, List<CommandExecuter> executer) {
		super(permission, permissionMessage, description);
		this.name = name;
		this.usage = usage;
		this.aliases = aliases;
		this.executer = executer;
		this.permission = permission;
		this.permissionMessage = permissionMessage;
	}
	
	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public String getUsage() {
		return usage;
	}

	public String getPermission() {
		return permission;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public List<CommandExecuter> getExecuter() {
		return executer;
	}

	@Override
	public String[] getCommandAliases() {
		return this.aliases;
	}
	
	@Override
	public String getCommandName() {
		return this.name;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return this.usage;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
		for (CommandExecuter exec : this.executer) {
			exec.onCommand(this, var1, var2);
		}
	}

	@Override
	public String getCommandAsString() {
		return this.name;
	}
	
	@Override
	   public boolean isVanillaCommand() { //CraftStone
		   return false;
	   }
	
}