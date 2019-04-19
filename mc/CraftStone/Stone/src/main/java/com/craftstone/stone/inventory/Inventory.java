package com.craftstone.stone.inventory;

import java.util.List;

/**
 * Holds an inventory
 * @author kmate
 *
 */
public interface Inventory {
	/**
	 * Returns the slots of this inventory
	 * @return the slot list
	 */
	public List<InventorySlot> getSlots();
	
	/**
	 * Returns the title of this inventory
	 * @return the title
	 */
	public String getName();
	
	/**
	 * Returns the size of this inventory
	 * @return the size
	 */
	public int getSize();
}
