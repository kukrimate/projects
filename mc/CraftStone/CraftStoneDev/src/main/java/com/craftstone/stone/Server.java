package com.craftstone.stone;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.base.Optional;
import com.craftstone.stone.command.CommandSender;
import com.craftstone.stone.entity.OfflinePlayer;
import com.craftstone.stone.entity.Player;
import com.craftstone.stone.plugin.PluginManager;
import com.craftstone.stone.world.World;

/**
 * Holds a server
 * @author kmate
 *
 */
public interface Server extends CommandSender
{
	/**
	 * Returns the adress of the server
	 * @return the adress
	 */
	public String getAdress();
	
	/**
	 * Returns the port of the server
	 * @return the port
	 */
	public int getPort();
	
	/**
	 * Returns is the server online mode
	 * @return online mode boolean
	 */
	public boolean isOnlineMode();
	
	/**
	 * Returns a boolean of is the server running
	 * @return status boolean
	 */
	public boolean isRunning();
	
	/**
	 * Returns string motd
	 * @return string motd
	 */
	public String getMotd();
	
	/**
	 * Sets the motd of the server
	 * @param motd the motd
	 */
	public void setMotd(String motd);
	
	/**
	 * Returns the max players of the server
	 * @return int max players
	 */
	public int getMaxPlayers();
	
	/**
	 * Returns the current online player amount
	 * @return current online player amount
	 */
	public int getCurrentOnlinePlayerCount();
	
	/**
	 * Broadcasts a message over the server
	 * @param message The message
	 */
	public void broadcastMessage(String message);
	
	/**
	 * Returns the logger
	 * @return the logger
	 */
	public Logger getLogger();
	
	/**
	 * The data folder of the server
	 * @return data folder
	 */
	public File getServerDataFolder();
	
	/**
	 * A list of the current online players
	 * @return
	 */
	public List<Player> getOnlinePlayers();
	
	/**
	 * A nullable method for gettings an online player
	 * @return the player
	 */
	@Nullable
	public Optional<Player> getPlayerIfOnline(String name);
	
	/**
	 * Nullable method for getting offline player
	 * @return the player
	 */
	@Nullable
	public Optional<OfflinePlayer> getPlayerIfExists(String name);
	
	/**
	 * Returns the plugin manager
	 * @return the plugin manager
	 */
	public PluginManager getPluginManager();
	
	/**
	 * A list of the worlds on the server
	 * @return the world list
	 */
	public List<World> getWorlds();
	
	/**
	 * Returns the a worlds
	 * @param name the name of the world
	 * @return the world
	 */
	@Nullable
	public Optional<World> getWorld(String name);
	
	/**
	 * Returns is nether allowed on the server
	 * @return the state
	 */
	public boolean isNetherAllowed();
	
	/**
	 * Returns is end allowed on the server
	 * @return the state
	 */
	public boolean isEndAllowed();
	
	/**
	 * Returns the api version
	 * @return the version
	 */
	public String getAPIVersion();
}
