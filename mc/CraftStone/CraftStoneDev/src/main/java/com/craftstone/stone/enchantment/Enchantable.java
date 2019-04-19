package com.craftstone.stone.enchantment;

import java.util.List;

/**
 * Holds an enchantable thing eg an item
 * @author kmate
 *
 */
public interface Enchantable {
	/**
	 * Returns a list of the enchantments
	 * @return the list
	 */
	public List<Enchantment> getEnchantments();
	
	/**
	 * Adds an enchantment
	 * @param enchantment
	 */
	public void addEnchantment(Enchantment enchantment);
}
