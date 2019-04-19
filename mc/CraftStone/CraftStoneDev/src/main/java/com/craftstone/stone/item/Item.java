package com.craftstone.stone.item;

import com.craftstone.stone.enchantment.Enchantable;

/**
 * Holds an item
 * @author kmate
 *
 */
public interface Item extends Enchantable {
	/**
	 * Returns the id of the item
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Returns the max stack size the item can reach
	 * @return the size
	 */
	public int getMaxStackSize();
	
	/**
	 * Returns the name of this item
	 * @return the name
	 */
	public String getName();
	
	/**
	 * This is used to set custom names for items
	 */
	public void setCustomName();
	
	/**
	 * Returns how demaged this item is
	 * @return the demage
	 */
	public double getDemage();
	
	/**
	 * Returns is this item is a tool
	 * @return the flag
	 */
	public boolean isTool();
}