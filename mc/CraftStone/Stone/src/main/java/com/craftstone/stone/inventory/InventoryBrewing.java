package com.craftstone.stone.inventory;

import java.util.List;

/**
 * Holds an inventory of a brewing stand
 * @author kmate
 *
 */
public interface InventoryBrewing extends Inventory {
	/**
	 * Returns the ingredient in this inventory
	 * @return the ingredient
	 */
	public InventorySlot getIngerdient();
	
	/**
	 * Returns the potion list of this inventory
	 * @return the list
	 */
	public List<InventorySlot> getPotions();
}
