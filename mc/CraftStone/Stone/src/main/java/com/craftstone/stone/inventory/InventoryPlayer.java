package com.craftstone.stone.inventory;

import java.util.List;

import com.craftstone.stone.entity.OfflinePlayer;

/**
 * Holds an inventory of a player
 * @author kmate
 *
 */
public interface InventoryPlayer extends Inventory {
	/**
	 * Returns the owner of the inventory
	 * @return the owner
	 */
	public OfflinePlayer getOwner();
	
	/**
	 * Returns a list of the inner slots of this inventory
	 * @return the list
	 */
	public List<InventorySlot> getInnerSlot();
	
	/**
	 * Returns a list of the outer slots of this inventory
	 * @return the list
	 */
	public List<InventorySlot> getOuterSlot();
}
