package com.craftstone.stone.inventory;

public interface InventoryFurnace extends InventorySlot {
	/**
	 * Returns the fuel currently in this inventory
	 * @return the fuel
	 */
	public InventorySlot getFuel();
	
	/**
	 * Returns the item stack in the to cook slot in this inventory 
	 * @return the item to cook
	 */
	public InventorySlot getItemStackToCook();
	
	/**
	 * Returns the itemstack in the result slot of this inventory
	 * @return the result
	 */
	public InventorySlot getResult();
}
