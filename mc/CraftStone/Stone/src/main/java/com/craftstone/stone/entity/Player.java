package com.craftstone.stone.entity;

import com.craftstone.stone.inventory.InventoryPlayer;

/**
 * Holds a player
 * @author kmate
 *
 */
public interface Player extends Entity {

	/**
	 * Returns the name of this player
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns the display name of this player
	 * @return the display name
	 */
	public String getPlayerDisplayName();
	
	/**
	 * Checks is the player have a permission
	 * @param permission the permission
	 * @return the flag
	 */
	public boolean hasPermission(String permission);
	
	/**
	 * Gives a permission to the player
	 * @param permission the permissions
	 */
	public void givePermission(String permission);
	
	/**
	 * Removes a permission from the player
	 * @param permission the permission
	 */
	public void removePermission(String permission);
	
	/**
	 * Returns is the player is an op
	 * @return
	 */
	public boolean isOp();
	
	public InventoryPlayer getInventory();
}
