package com.craftstone.stone.plugin;

import java.io.File;

import com.craftstone.stone.Server;
import com.craftstone.stone.command.CommandExecuter;

public interface BasePlugin extends CommandExecuter
{
	/**
	 * Method gets called on plugin enabling
	 */
	public void onEnable();
	
	/**
	 * Method gets called on plugin disable
	 */
	public void onDisable();
	
	/**
	 * Method gets called on plugin reload
	 */
	public void onReload();
	
	/**
	 * Returns the data folder of the plugin
	 * @return the data folder
	 */
	public File getDataFolder();
	
	/**
	 * Returns a string version number for the plugin
	 * @return the version
	 */
	public String getVersion();
	
	/**
	 * Returns the string name of the plugin
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns the server of the plugin
	 * @return the server
	 */
	public Server getServer();
}