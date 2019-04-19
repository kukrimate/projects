package com.craftstone.stone.enchantment;

import java.util.List;

/**
 * Holds an enchantment
 * @author kmate
 *
 */
public interface Enchantment {
	/**
	 * Returns the id of this enchantment
	 * @return the id
	 */
	public int getID();
	
	/**
	 * Returns the name of this enchantment
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns other enchantment that this enchantment can be with on one item
	 * @return
	 */
	public List<Enchantment> getCompatibleEnchantments();
}
