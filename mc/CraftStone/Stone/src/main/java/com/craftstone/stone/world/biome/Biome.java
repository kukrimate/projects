package com.craftstone.stone.world.biome;

import java.util.List;

import com.craftstone.stone.block.Block;
import com.craftstone.stone.entity.Entity;

/**
 * Holds a biome
 * @author kmate
 *
 */
public interface Biome {
	/**
	 * The biome temperature
	 * @return the temperature
	 */
	public float getTemperature();
	
	/**
	 * The biome name
	 * @return the name
	 */
	public String getName();
	
	/**
	 * The biome id
	 * @return the id
	 */
	public double getID();
	
	/**
	 * The biome color
	 * @return the color
	 */
	public int getColor();
	
	/**
	 * Returns the top block
	 * @return the block
	 */
	public Block getTopBlock();

	/**
	 * Returns the filler block
	 * @return the block
	 */
	public Block getFillerBlock();
	
	/**
	 * Returns the max height of the biome
	 * @return the height
	 */
	public float getMaxHeight();
	
	/**
	 * Returns the min height of the biome
	 * @return the height
	 */
	public float getMinHeight();
	
	/**
	 * Returns the rainfall duration for this biome
	 * @return the duration
	 */
	public float getRainfall();
	
	/**
	 * Returns the color multiplier for the water in this biome
	 * @return the multiplier
	 */
	public int getWaterColorMultiplier();
	
	/**
	 * Is rain enabled in this biome
	 * @return the boolean of the status
	 */
	public boolean getEnableRain();
	
	/**
	 * Is snow enabled in this biome
	 * @return the boolean of the status
	 */
	public boolean getEnableSnow();
	
	/**
	 * A list of spawnable animals like pig, sheep, cow etc.
	 * @return the list
	 */
	public List<Entity> getSpawnableFirendlyMobs();
	
	/**
	 * The list of spawnable monsters like zombie, skeleton etc.
	 * @return the list
	 */
	public List<Entity> getSpawnableHostileMobs();
	
	/**
	 * A list of spawnable water creatures
	 * @return the list
	 */
	public List<Entity> getSpawnableWaterMobs();
}
