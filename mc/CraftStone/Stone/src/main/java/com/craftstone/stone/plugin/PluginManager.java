package com.craftstone.stone.plugin;

import javax.annotation.Nullable;

import com.craftstone.stone.command.CommandExecuter;
import com.craftstone.stone.event.Listener;
import com.google.common.base.Optional;

/**
 * The plugin manager
 * @author kmate
 *
 */
public interface PluginManager {
	/**
	 * Registers a command executer
	 * @param executer the executer
	 * @param commandAlieses the alieses of the executer
	 */
	public void registerCommandExecuter(CommandExecuter executer, String command);
	
	/**
	 * Registers a listiner to the given plugin
	 * @param listener the listener
	 * @param plugin the plugin
	 */
	public void registerListener(Listener listener, BasePlugin plugin);
	
	/**
	 * Returns a plugin based on name
	 * @param name the name
	 * @return the plugin
	 */
	@Nullable
	public Optional<BasePlugin> getPlugin(String name);
}