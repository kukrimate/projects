package com.craftstone.stone.potion;

import java.util.List;

import com.craftstone.stone.item.Item;

/**
 * Holds a potion
 * @author kmate
 *
 */
public interface Potion extends Item {
	/**
	 * Returns the list of effects on this potion item
	 * @return the list
	 */
	public List<PotionEffect> getEffects();
	
	/**
	 * Is this a droppable potion
	 * @return the boolean
	 */
	public boolean isDroppable();
	
	/**
	 * Add a potion effect on this potion item
	 * @param effect the effect
	 */
	public void addEffect(PotionEffect effect);
}