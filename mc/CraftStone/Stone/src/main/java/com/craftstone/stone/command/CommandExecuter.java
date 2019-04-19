package com.craftstone.stone.command;

import com.craftstone.stone.plugin.BasePlugin;

/**
 * Executer class for commands
 * @author kmate
 *
 */
public interface CommandExecuter {
	/**
	 * Called on command execution
	 * @param command The command
	 * @param sender The command sender
	 */
	public boolean onCommand(Command command, CommandSender sender, String[] args);
	
	/**
	 * Returns the owner of this executer
	 * @return the owner
	 */
	public BasePlugin getPlugin();
}