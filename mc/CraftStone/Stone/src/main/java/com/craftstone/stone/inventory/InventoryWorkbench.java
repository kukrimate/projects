package com.craftstone.stone.inventory;

import java.util.List;

/**
 * Holds a workbench inventory
 * @author kmate
 *
 */
public interface InventoryWorkbench extends Inventory {
	/**
	 * Returns the slot list of ingredient slots of this workbench
	 * @return the slots
	 */
	public List<InventorySlot> getIngerdients();
	
	/**
	 * Returns the result slot of this inventory
	 * @return the slot
	 */
	public InventorySlot getResult();
}
