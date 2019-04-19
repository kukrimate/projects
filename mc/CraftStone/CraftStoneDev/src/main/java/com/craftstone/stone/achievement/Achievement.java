package com.craftstone.stone.achievement;

import com.craftstone.stone.item.Item;

/**
 * Holds an acheivement
 * @author kmate
 *
 */
public interface Achievement {
	/**
	 * Returns the item used to draw the achievement on the gui
	 * @return the item
	 */
	public Item getIconItem();
	
	/**
	 * Returns is the command spacial
	 * @return boolean status
	 */
	public boolean isSpecial();
	
	/**
	 * Returns the parent achievement of the achievement
	 * @return the parent
	 */
	public Achievement getParent();
	
	/**
	 * Returns the name of the achievement
	 * @return The name
	 */
	public String getName();
	
	/**
	 * Returns the description if the achievement
	 * @return The description
	 */
	public String getDescription();
}
