package com.craftstone.stone.inventory;

import javax.annotation.Nullable;

import com.craftstone.stone.item.ItemStack;
import com.google.common.base.Optional;

/**
 * Holds an inventory slot
 * @author kmate
 *
 */
public interface InventorySlot {
	/**
	 * Returns the item stack holden by this slot
	 * @return the itemstack
	 */
	@Nullable
	public Optional<ItemStack> getItemStackInSlot();
}
