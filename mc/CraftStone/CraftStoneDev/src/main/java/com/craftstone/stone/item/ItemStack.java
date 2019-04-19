package com.craftstone.stone.item;

import javax.annotation.Nullable;

import com.craftstone.stone.inventory.Inventory;
import com.google.common.base.Optional;

/**
 * Holds an item stack
 * @author kmate
 *
 */
public interface ItemStack {
	/**
	 * Returns how many items are in this stack
	 * @return the items number
	 */
	public int getAmount();
	
	/**
	 * Returns what item is this stack from
	 * @return the item
	 */
	public Item getItem();
	
	/**
	 * Sets this stacks item
	 * @param item The item
	 */
	public void setItem(Item item);
	
	/**
	 * Sets this stacks amount
	 * @param amount The amount
	 */
	public void setAmount(int amount);
	
	/**
	 * Returns the max size of stack
	 * @return the max size
	 */
	public int getMaxSize();
	
	/**
	 * Returns the inventory if this stack in one or null if not
	 * @return the inventory
	 */
	@Nullable
	public Optional<Inventory> getInventory();
	
	public double getDurability();
}
