package com.craftstone.stone.potion;

/**
 * Holds a potion effect
 * @author kmate
 *
 */
public interface PotionEffect {
	/**
	 * Returns the id of the effect
	 * @return the id
	 */
	public int getID();
	
	/**
	 * Returns the level of the effect
	 * @return the level
	 */
	public int getLevel();
	
	/**
	 * Returns the name of the effect
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns the lenght of the effect
	 * @return the lenght
	 */
	public int getLenght();
}
