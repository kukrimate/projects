package com.craftstone.stone.block;

import com.craftstone.stone.item.Item;

/**
 * Holds an item of a block when breaked
 * @author kmate
 *
 */
public interface BlockItem extends Item {
	/**
	 * Returns the block of this item
	 * @return the block
	 */
	public Block getBlock();
}
