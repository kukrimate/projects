package com.craftstone.stone.block;

import com.craftstone.stone.math.VectorDouble;

/**
 * Holds a block
 * @author kmate
 *
 */
public interface Block {
	/**
	 * Returns the id of this block
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Returns the position of this block
	 * @return the position
	 */
	public VectorDouble getPosition();

	/**
	 * Returns the name of this block
	 * @return the name
	 */
	public String getName();
}
