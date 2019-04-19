package com.craftstone.stone.entity;

/**
 * Holds a player that is not chacked is online
 * @author kmate
 *
 */
public interface OfflinePlayer {
	/**
	 * Checks is the player online
	 * @return the flag
	 */
	public boolean isOnline();
	
	/**
	 * Returns the username of the player
	 * @return the name
	 */
	public String getName();
}